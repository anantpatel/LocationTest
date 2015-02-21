package com.example.locationtest;




//import com.example.android.location.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements 
			GooglePlayServicesClient.ConnectionCallbacks, 
			GooglePlayServicesClient.OnConnectionFailedListener,
			LocationListener{

	
	/*-----------------------UI ----------------------------------------*/
	ActionBar.Tab 	Tab1, Tab2, Tab3;
	Location_FragTab 	FragTab1 = new Location_FragTab();
	Profile_FragTab 	FragTab2 = new Profile_FragTab();
	/*-------------------------------------------------------------------*/
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
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
	private LocationClient m_LocationClient;
	// Global variable to hold the current location
    //private Location mCurrentLocation;
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    
    boolean mUpdatesRequested;
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*  Create Tabs */
		ActionBar actionBar = getSupportActionBar();
		
		   // Hide Actionbar Icon
	     actionBar.setDisplayShowHomeEnabled(false);
	
	     // Hide Actionbar Title
	     actionBar.setDisplayShowTitleEnabled(false);
	
	     // Create Actionbar Tabs
	     actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
	     // Set Tab Icon and Titles
	     Tab1 = actionBar.newTab().setText("Locations");
	     Tab2 = actionBar.newTab().setText("Profiles");
	     
	     // Set Tab Listeners
	  	Tab1.setTabListener( new TabListener(FragTab1));
	  	Tab2.setTabListener(new TabListener(FragTab2));
	  
	     
	     actionBar.addTab(Tab1);
	     actionBar.addTab(Tab2);
	    //End of Tab Creation
	    /********************************************************************/
		setContentView(R.layout.activity_main);
		
			    
		  // Open the shared preferences
        mPrefs = getSharedPreferences("SharedPreferences",
                Context.MODE_PRIVATE);
        // Get a SharedPreferences editor
        mEditor = mPrefs.edit();
		 /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        m_LocationClient = new LocationClient(this, this, this);
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
        
	}
	/*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        m_LocationClient.connect();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
    	// If the client is connected
        if (m_LocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            removeLocationUpdates(this);
        }
        // Disconnecting the client invalidates it.
        m_LocationClient.disconnect();
        super.onStop();
    }
    
    private void removeLocationUpdates(MainActivity mainActivity) {
		// TODO Auto-generated method stub
    	  m_LocationClient.removeLocationUpdates(this);
    	  
	}
	@Override
    protected void onPause() {
        // Save the current setting for updates
        mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
        mEditor.commit();
        super.onPause();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;
		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
		    super();
		    mDialog = null;
		}
		// Set the dialog to display
		public void setDialog(Dialog dialog) {
		    mDialog = dialog;
		}
		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		    return mDialog;
		}
	}
	
	/*
	* Handle results returned to the FragmentActivity
	* by Google Play services
	*/
	@Override
	protected void onActivityResult(
	    int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		   
		    case CONNECTION_FAILURE_RESOLUTION_REQUEST :
		    /*
		     * If the result code is Activity.RESULT_OK, try
		     * to connect again
		     */
		        switch (resultCode) {
		            case Activity.RESULT_OK :
		            /*
		             * Try the request again
		             */
		           
		            break;
		        }
		    
		}
	}
	
	
	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //mUpdatesRequested = true;
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            m_LocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

	private void showErrorDialog(int errorCode) {
		// TODO Auto-generated method stub
		 GooglePlayServicesUtil.getErrorDialog(errorCode, this, 
			      REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
	}
	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	 	    
}
