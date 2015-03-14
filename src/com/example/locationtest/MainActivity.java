package com.example.locationtest;

import com.helper.*;

//import com.example.android.location.LocationUtils;
import com.example.locationtest.NoticeDialogFragment.NoticeDialogListener;
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
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.CalendarAlerts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements 
			GooglePlayServicesClient.ConnectionCallbacks, 
			GooglePlayServicesClient.OnConnectionFailedListener,
			LocationListener{

	
	/*-----------------------UI ----------------------------------------*/
	ActionBar.Tab 	Tab1, Tab2, Tab3;
	Location_FragTab 	FragTab1 = new Location_FragTab();
	Profile_FragTab 	FragTab2 = new Profile_FragTab();
	Rules_FragTab		FragTab3 = new Rules_FragTab();
	/*-------------------------------------------------------------------*/
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private final static String TAG = "MainActivity";
	static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	// Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 15;
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
		
		TestHelper.createTestForRules(getApplicationContext());
		
		
		SettingsManager.getInstance().setContext(getApplicationContext());
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
	     Tab3 = actionBar.newTab().setText("Rules");
	     
	     // Set Tab Listeners
	  	Tab1.setTabListener( new TabListener(FragTab1));
	  	Tab2.setTabListener(new TabListener(FragTab2));
	  	Tab3.setTabListener(new TabListener(FragTab3));
	  
	     
	     actionBar.addTab(Tab1);
	     actionBar.addTab(Tab2);
	     actionBar.addTab(Tab3);
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
        mUpdatesRequested = true;
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        
        // creating and register receiver
 		ReminderTest receiver = new ReminderTest();
 		IntentFilter EventFilter = new IntentFilter(CalendarContract.ACTION_EVENT_REMINDER);
 		EventFilter.addDataScheme("content");
 		registerReceiver(receiver, EventFilter);
 		
        
	}
	
	public class ReminderTest extends BroadcastReceiver {
		
		final String[] ALERT_PROJECTION = new String[] { CalendarAlerts._ID, // 0
				CalendarAlerts.EVENT_ID, // 1
				CalendarAlerts.STATE, // 2
				CalendarAlerts.TITLE, // 3
				CalendarAlerts.EVENT_LOCATION, // 4
				CalendarAlerts.SELF_ATTENDEE_STATUS, // 5
				CalendarAlerts.ALL_DAY, // 6
				CalendarAlerts.ALARM_TIME, // 7
				CalendarAlerts.MINUTES, // 8
				CalendarAlerts.BEGIN, // 9
				CalendarAlerts.END, // 10
				CalendarAlerts.DESCRIPTION, // 11
				CalendarAlerts.DURATION, // 12

		};
		
		private static final int ALERT_INDEX_EVENT_ID = 1;
		private static final int ALERT_INDEX_TITLE = 3;
		/*private static final int ALERT_INDEX_ID = 0;
		private static final int ALERT_INDEX_STATE = 2;		
		private static final int ALERT_INDEX_EVENT_LOCATION = 4;
		private static final int ALERT_INDEX_SELF_ATTENDEE_STATUS = 5;
		private static final int ALERT_INDEX_ALL_DAY = 6;
		private static final int ALERT_INDEX_ALARM_TIME = 7;
		private static final int ALERT_INDEX_MINUTES = 8;
		private static final int ALERT_INDEX_DURATION = 12;
		*/
		private static final int ALERT_INDEX_BEGIN = 9;
		private static final int ALERT_INDEX_END = 10;
		private static final int ALERT_INDEX_DESCRIPTION = 11;
		
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(CalendarContract.ACTION_EVENT_REMINDER)) {
				//Toast.makeText(getApplicationContext(), "Recieved Event", Toast.LENGTH_SHORT).show();
				
				//Fetch eventId
				// Sync CalendarAlerts with global dismiss cache before query it
				ContentResolver cr = context.getContentResolver();
				
				long currentTime = System.currentTimeMillis();
				

				final String ACTIVE_ALERTS_SELECTION = "(" + CalendarAlerts.STATE + "=? OR " + CalendarAlerts.STATE + "=?) AND "
						+ "(" + CalendarAlerts.ALARM_TIME + "<=?) AND " 
						+ "(" + CalendarAlerts.ALARM_TIME + ">=?)";
				
				final String[] ACTIVE_ALERTS_SELECTION_ARGS = new String[] { Integer.toString(CalendarAlerts.STATE_SCHEDULED),
						Integer.toString(CalendarAlerts.STATE_SCHEDULED), Long.toString(currentTime), Long.toString(currentTime - 2000) };
				
				final String ACTIVE_ALERTS_SORT = "end DESC, begin DESC";

				Cursor alertCursor = cr.query(CalendarAlerts.CONTENT_URI, ALERT_PROJECTION, (ACTIVE_ALERTS_SELECTION),
						ACTIVE_ALERTS_SELECTION_ARGS, ACTIVE_ALERTS_SORT);

				
				if (alertCursor == null || alertCursor.getCount() == 0) {
					if (alertCursor != null) {
						alertCursor.close();
					}
					return;
				}
				Toast.makeText(getApplicationContext(), "Recieved Event" + alertCursor.getCount(), Toast.LENGTH_SHORT).show();
				
				Bundle bundle = (Bundle) new Bundle();

				while (alertCursor.moveToNext()) 
				{
					final long eventId = alertCursor.getLong(ALERT_INDEX_EVENT_ID);
					final String eventName = alertCursor.getString(ALERT_INDEX_TITLE);
					final String description = alertCursor.getString(ALERT_INDEX_DESCRIPTION);
					final long beginTime = alertCursor.getLong(ALERT_INDEX_BEGIN);
					final long endTime = alertCursor.getLong(ALERT_INDEX_END);				
					//final String[] splitDesc = description.split("\\s+");

					bundle.putLong("eventId", eventId);
					bundle.putString("eventName", eventName);
					bundle.putString("eventDesc", description);
					bundle.putLong("beginTime", beginTime);
					bundle.putLong("endTime", endTime);
					bundle.putString("type", RulesManager.CALENDAR_TYPE );
					
					Log.i("Profile", bundle.toString());
					
					/*AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
					Intent i = new Intent(context, RulesManager.class);
					i.putExtras(bundle);
					PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);
					Intent i = new Intent(context, Rules_FragTab.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					i.putExtras(bundle);
					context.startActivity(i);*/
					
					RulesManager.getInstance().checkNewEvent( bundle);
				}
				//Fetch Title, begin time
				
				//Set Alarm
			}
			
			
		}
		
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
        	startPeriodicUpdates();
        	 if (servicesConnected())
        	 {
        		 m_LocationClient.requestLocationUpdates(mLocationRequest, this);
        	 }
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
		if( location != null)
		{
	        String msg = "Updated Location: " +
	                Double.toString(location.getLatitude()) + "," +
	                Double.toString(location.getLongitude());
	        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	        FragTab3.onLocationChanged(location);
		}
		else
		{
			Log.i(TAG,"Location NULL");
		}
	}
	
	/**
     * Invoked by the "Start Updates" button
     * Sends a request to start location updates
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void startUpdates(View v) {
        mUpdatesRequested = true;

        if (servicesConnected()) {
        	startPeriodicUpdates();
        }
    }
    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
    	m_LocationClient.requestLocationUpdates(mLocationRequest, this);
    	//mConnectionStatus.setText("Location Requested");
    }
    /**
     * Invoked by the "Stop Updates" button
     * Sends a request to remove location updates
     * request them.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void stopUpdates(View v) {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }
    
    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
    	m_LocationClient.removeLocationUpdates(this);
    	//mConnectionStatus.setText("Stopped");
    }
	
	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode =
		        GooglePlayServicesUtil.
		                isGooglePlayServicesAvailable(this);
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
		            this,
		            CONNECTION_FAILURE_RESOLUTION_REQUEST);
		
		    // If Google Play services can provide an error dialog
		    if (errorDialog != null) {
		        // Create a new DialogFragment for the error dialog
		        ErrorDialogFragment errorFragment =
		                new ErrorDialogFragment();
		        // Set the dialog in the DialogFragment
		        errorFragment.setDialog(errorDialog);
		        // Show the error dialog in the DialogFragment
		        errorFragment.show(getSupportFragmentManager(),
		                "Location Updates");
		        
		    }
		    
		    return false;
		}
	}
	 	    
}
