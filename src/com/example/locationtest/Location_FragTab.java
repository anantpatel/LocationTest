package com.example.locationtest;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.locationtest.MainActivity.ErrorDialogFragment;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView.OnItemLongClickListener;


import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Color;

public class Location_FragTab extends Fragment implements 
	GooglePlayServicesClient.ConnectionCallbacks, 
	GooglePlayServicesClient.OnConnectionFailedListener,
	LocationListener ,
	View.OnClickListener
	{
		//Context 
		ViewGroup m_Container;
		LayoutInflater m_Layout;
		LocationAdaptor LocationAdpt;
		private ListView locationlstView;
		SettingsManager m_SettingMgr;
		
		//Location Based Members
		private LocationClient m_LocationClient;
		// Global variable to hold the current location
	    private Location mCurrentLocation;
	    // Define an object that holds accuracy and frequency parameters
	    LocationRequest mLocationRequest;
	    boolean mUpdatesRequested;
	    
		// Handles to UI widgets
	    private TextView mLatLng;
	    private TextView mAddress;
	    private ProgressBar mActivityIndicator;
	    private TextView mConnectionStatus;

		protected List<LocationInfo> m_LocationProfileMap;
	    
	    
	    //Constants
		static final String PREF_LOCATIONMAP = "LocationProfileMap";
	    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	    
	    // Milliseconds per second
	    private static final int MILLISECONDS_PER_SECOND = 1000;
	    // Update frequency in seconds
	    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	    // Update frequency in milliseconds
	    private static final long UPDATE_INTERVAL =
	            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	    // The fastest update frequency, in seconds
	    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	    // A fast frequency ceiling in milliseconds
	    private static final long FASTEST_INTERVAL =
	            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	    
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
			 View rootView = inflater.inflate(R.layout.location_frag, container, false);
			 
			 //Create Instances Locations related member variables
			 m_Container = container;
			 m_Layout = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 m_LocationClient = new LocationClient(container.getContext(), this,this ); 
			 m_LocationClient.connect();
			 
			// Start with updates turned off
			mUpdatesRequested = false;
			// Create the LocationRequest object
			mLocationRequest = LocationRequest.create();
			// Use high accuracy
			mLocationRequest.setPriority(
			        LocationRequest.PRIORITY_HIGH_ACCURACY);
			// Set the update interval to 5 seconds
			mLocationRequest.setInterval(UPDATE_INTERVAL);
			// Set the fastest update interval to 1 second
			mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
			
			 // UI Init
			 mLatLng = (TextView)rootView.findViewById(R.id.lat_lng);
			 mAddress = (TextView)rootView.findViewById(R.id.address);
			 mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.address_progress);
			 //Assign Callback 
			 Button btn_getLocation = (Button) rootView.findViewById(R.id.get_location_button);
			 btn_getLocation.setOnClickListener((OnClickListener) this);
			 
			 Button btn_getAddress = (Button) rootView.findViewById(R.id.get_address_button);
			 btn_getAddress.setOnClickListener((OnClickListener) this);
			 
			 Button btn_startUpdate = (Button) rootView.findViewById(R.id.start_updates);
			 btn_startUpdate.setOnClickListener((OnClickListener) this);
			 
			 Button btn_stopUpdate = (Button) rootView.findViewById(R.id.stop_updates);
			 btn_stopUpdate.setOnClickListener((OnClickListener) this);
			 
			 Button btn_addLocation = (Button)rootView.findViewById(R.id.btn_addlocation);
			 btn_addLocation.setOnClickListener((OnClickListener)this);
			 
			 locationlstView = (ListView) rootView.findViewById(R.id.lv_location);
			 
			 //Fetch locations from Sharedpreference.
			 m_SettingMgr = SettingsManager.getInstance();
			 
			 m_LocationProfileMap = m_SettingMgr.getLocationProfileMapping(getActivity());
			
			 if( m_LocationProfileMap != null )
			 {
				 setLocationListUI();
			 }
			 //startUpdates(null);
		    return rootView;
			 
		 }
		
		/**
		 * Invoked by the "Get Location" button.
		 *
		 * Calls getLastLocation() to get the current location
		 *
		 * @param v The view object associated with this method, in this case a Button.
		 */
		public Location getLocation(View v) {
			// If Google Play Services is available
		   if (servicesConnected()) {
		
		        // Get the current location
		        Location currentLocation = m_LocationClient.getLastLocation();
				        
		        return currentLocation;
		    }
		   return null;
		}
		
		/**
		    * A subclass of AsyncTask that calls getFromLocation() in the
		    * background. The class definition has these generic types:
		    * Location - A Location object containing
		    * the current location.
		    * Void     - indicates that progress units are not used
		    * String   - An address passed to onPostExecute()
		    */
		    private class GetAddressTask extends
		            AsyncTask<Location, Void, String> {
		        Context mContext;
		        public GetAddressTask(Context context) {
		            super();
		            mContext = context;
		        }
		        /**
		         * Get a Geocoder instance, get the latitude and longitude
		         * look up the address, and return it
		         *
		         * @params params One or more Location objects
		         * @return A string containing the address of the current
		         * location, or an empty string if no address can be found,
		         * or an error message
		         */
		        @Override
		        protected String doInBackground(Location... params) {
		            Geocoder geocoder =
		                    new Geocoder(mContext, Locale.getDefault());
		            // Get the current location from the input parameter list
		            Location loc = params[0];
		            // Create a list to contain the result address
		            List<Address> addresses = null;
		            try {
		                /*
		                 * Return 1 address.
		                 */
		                addresses = geocoder.getFromLocation(loc.getLatitude(),
		                        loc.getLongitude(), 1);
		            } catch (IOException e1) {
		            Log.e("LocationSampleActivity",
		                    "IO Exception in getFromLocation()");
		            e1.printStackTrace();
		            return ("IO Exception trying to get address");
		            } catch (IllegalArgumentException e2) {
		            // Error message to post in the log
		            String errorString = "Illegal arguments " +
		                    Double.toString(loc.getLatitude()) +
		                    " , " +
		                    Double.toString(loc.getLongitude()) +
		                    " passed to address service";
		            Log.e("LocationSampleActivity", errorString);
		            e2.printStackTrace();
		            return errorString;
		            }
		            // If the reverse geocode returned an address
		            if (addresses != null && addresses.size() > 0) {
		                // Get the first address
		                Address address = addresses.get(0);
		                /*
		                 * Format the first line of address (if available),
		                 * city, and country name.
		                 */
		                String addressText = String.format(
		                        "%s, %s, %s",
		                        // If there's a street address, add it
		                        address.getMaxAddressLineIndex() > 0 ?
		                                address.getAddressLine(0) : "",
		                        // Locality is usually a city
		                        address.getLocality(),
		                        // The country of the address
		                        address.getCountryName());
		                // Return the text
		                return addressText;
		            } else {
		                return "No address found";
		            }
		        }
		        /**
		         * A method that's called once doInBackground() completes. Turn
		         * off the indeterminate activity indicator and set
		         * the text of the UI element that shows the address. If the
		         * lookup failed, display the error message.
		         */
		        @Override
		        protected void onPostExecute(String address) {
		            // Set activity indicator visibility to "gone"
		            mActivityIndicator.setVisibility(View.GONE);
		            // Display the results of the lookup.
		            mAddress.setText(address);
		        }
		    }

		/**
	     * The "Get Address" button in the UI is defined with
	     * android:onClick="getAddress". The method is invoked whenever the
	     * user clicks the button.
	     *
	     * @param v The view object associated with this method,
	     * in this case a Button.
	     */
	    @SuppressLint("NewApi")
		public void getAddress(View v) {
	        // Ensure that a Geocoder services is available
	        if (Build.VERSION.SDK_INT >=
	                Build.VERSION_CODES.GINGERBREAD
	                            &&
	                Geocoder.isPresent()) {
	            // Show the activity indicator
	            mActivityIndicator.setVisibility(View.VISIBLE);
	            // Get the current location
	            Location currentLocation = m_LocationClient.getLastLocation();

	            /*
	             * Reverse geocoding is long-running and synchronous.
	             * Run it on a background thread.
	             * Pass the current location to the background task.
	             * When the task finishes,
	             * onPostExecute() displays the address.
	             */
	            (new GetAddressTask(v.getContext())).execute(currentLocation);
	        }
	    }
		
		private boolean servicesConnected() {
			// Check that Google Play services is available
			int resultCode =
			        GooglePlayServicesUtil.
			                isGooglePlayServicesAvailable(m_Container.getContext());
			// If Google Play services is available
			if (ConnectionResult.SUCCESS == resultCode) {
			    // In debug mode, log the status
			    Log.d("Location Updates",
			            "Google Play services is available.");
			    // Continue
			    return true;
			// Google Play services was not available for some reason.
			// resultCode holds the error code.
			} else {
			    // Get the error dialog from Google Play services
			    Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
			            resultCode,
			            (Activity) m_Container.getContext(),
			            CONNECTION_FAILURE_RESOLUTION_REQUEST);
			
			    // If Google Play services can provide an error dialog
			    if (errorDialog != null) {
			        // Create a new DialogFragment for the error dialog
			        ErrorDialogFragment errorFragment =
			                new ErrorDialogFragment();
			        // Set the dialog in the DialogFragment
			        errorFragment.setDialog(errorDialog);
			        // Show the error dialog in the DialogFragment
			        errorFragment.show(getFragmentManager(),
			                "Location Updates");
			        
			    }
			    
			    return false;
			}
		}
		
		private void showErrorDialog(int errorCode) {
			// TODO Auto-generated method stub
			 GooglePlayServicesUtil.getErrorDialog(errorCode, (Activity) m_Container.getContext(), 
				      REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
		}

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnected(Bundle arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 switch(v.getId()){
	            case R.id.get_location_button:
	            	getLocation( v );
	            break;
	            case R.id.get_address_button:
	            	getAddress(v);
	            break;
	          /*  case R.id.start_updates:
	            	startUpdates(v);
	            	break;
	            case R.id.stop_updates:
	            	stopUpdates(v);
	            	break;*/
	            case R.id.btn_addlocation:
	            	addLocation(v);
	            	break;
	        }   
			
		}
		
		
		private void addLocation(View v) {
			// TODO Auto-generated method stub
			showNewLocationUI();
		}

		private void showNewLocationUI() {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    // Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    final View vi = inflater.inflate(R.layout.dialog_additem, null);
		    final EditText edittxt = (EditText) vi.findViewById(R.id.dialog_Text);
		    edittxt.setHint("Location Name");
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(vi)
		    // Add action buttons
		           .setPositiveButton("Create", new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   	            	   
		            	   //Create new locationInfo
		            	   saveLocation(edittxt.getText().toString());
		            	   
		            	
		               }
		           })
		           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                   //this.getDialog().cancel();
		            	   dialog.cancel();
		               }
		           });      
		     builder.setTitle("Create Location");
		     
		     
		     builder.create().show();
		}

		protected void saveLocation(String location) {
			// TODO Auto-generated method stub
			//Fetch current Location
			Location currentLocation = getLocation(null);
			//Create location info
			LocationInfo locationinfo = new LocationInfo(location, 
        			   "Normal", 
        			   currentLocation.getLongitude(), 
        			   currentLocation.getLatitude());
			//Add locationinfo to Profile Map;
			if(m_LocationProfileMap == null)
			{
				m_LocationProfileMap = new ArrayList<LocationInfo>();
			}
			/*LocationInfo locationinfo2 = new LocationInfo("Home", 
     			   "Silent", -121.150346,38.646512);
        	
        	m_LocationProfileMap.add(locationinfo2);*/
			m_LocationProfileMap.add(locationinfo);
			m_SettingMgr.saveLocationProfileMapping(getActivity(), m_LocationProfileMap);
	       
        	//Update UI
        	setLocationListUI();
			
		}

		protected void setLocationListUI() {
			// TODO Auto-generated method stub
			String[] strarray = new String[m_LocationProfileMap.size()];
			LocationAdpt = new LocationAdaptor(m_Container.getContext(), R.id.locationListRow, R.id.locationName, strarray);
			locationlstView.setAdapter(LocationAdpt);
			
			OnItemLongClickListener mLongClickHandler = new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					//TODO Add Popup for EDIT/DELETE Option
					locationActionDialog(position);
					//editProfile(position);
					return false;
				}
			};
			
			locationlstView.setOnItemLongClickListener(mLongClickHandler);
		}
		
		protected void locationActionDialog(final int position) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    // Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    final View vi = inflater.inflate(R.layout.dialog_additem, null);
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(vi)
		    // Add action buttons
		           .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   // sign in the user ...
		            	   deleteLocation(position);
		               }
		           })
		           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                   dialog.cancel();
		               }
		           });      
		     builder.setTitle("Create Profile");
		     
		     
		     builder.create().show();
			
		}

		protected void deleteLocation(int position) {
			// TODO Auto-generated method stub
			if( position >= 0 && position < m_LocationProfileMap.size())
			{
				m_LocationProfileMap.remove(position);				
				
				m_SettingMgr.saveLocationProfileMapping(getActivity(), m_LocationProfileMap);
				setLocationListUI();
			}
		}

		private class LocationAdaptor extends ArrayAdapter<String>
		{

			public LocationAdaptor(Context context, int resource,
					int textViewResourceId,String[] objects) {
				super(context, resource, textViewResourceId, objects);
				// TODO Auto-generated constructor stub
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				View view = m_Layout.inflate(R.layout.location_list_row, parent, false);
				/*TextView txtView = (TextView) view.findViewById(R.id.profileName);
				txtView.setText(m_LocationProfileMap.get(position).getProfileName());
				txtView.setTextColor(Color.BLACK);*/
				
				TextView txtView = (TextView) view.findViewById(R.id.locationName);
				txtView.setText(m_LocationProfileMap.get(position).getLocationName());
				
				txtView = (TextView) view.findViewById(R.id.locationvalue);
				txtView.setText(m_LocationProfileMap.get(position).getLocationValue());
				
				convertView = view;
				convertView.setBackgroundColor(Color.WHITE);
				return convertView;
			
			}
			
			
		}

		

	    @Override
	    public void onResume()
	    {
	    	//setLocationListUI();
	    	super.onResume();
	    
	    }
}
