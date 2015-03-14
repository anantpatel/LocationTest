package com.example.locationtest;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.locationtest.NoticeDialogFragment.NoticeDialogListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class Profile_FragTab extends Fragment implements
View.OnClickListener,
NoticeDialogListener
{
	
	private View m_Container;
	private ListView profilelstView;
	LayoutInflater m_Layout;	
	ProfileAdaptor profileAdpt;
	SettingsManager m_SettingMgr;
	private static FragmentActivity instance;
	/************** Test Purpose ( Delete Later ) ********/
	static final int MAX_PROFILE = 5; 
	List<String> ProfileNames = null;  
	String[] defaultProfName = {"Normal", "Silent", "LOW", "Hospital", "Movie"};
    /******* End Test Purpose***************/
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	 View rootView = inflater.inflate(R.layout.profile_frag, container, false);
        
	 m_Container = container;
	 profilelstView = (ListView) rootView.findViewById(R.id.lv_profiles);
	 m_Layout = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 Button btn_addProfile = ( Button) rootView.findViewById(R.id.btn_addprofile);
	 btn_addProfile.setOnClickListener((OnClickListener) this);
	 Profile_FragTab.instance= getActivity();
	 m_SettingMgr = SettingsManager.getInstance();
     
	 //Load Profile Names from Preferences
	 LoadProfiles();
	 
	 //Populate the Profile Data onto the UI
	 setProfileListUI();
	 
	 return rootView;
        
 }
	
	private void LoadProfiles()
	{
		ProfileNames= m_SettingMgr.getProfileNames(getActivity());
		 //Populated Default ProfileNames
		 if( ProfileNames  == null)
		 {
			 ProfileNames = new ArrayList<String>();
			 for ( int i = 0; i < defaultProfName.length ; i++)
			 {
				 ProfileNames.add(defaultProfName[i]);
			 }
			 
			 //Save the defaults ones to preference and now on ways use preferences.
			 m_SettingMgr.saveProfileNames(getActivity(), ProfileNames);
		 }
	}
	
	private void setProfileListUI()
	{		
		//Setup 
		String[] strarray = new String[ProfileNames.size()];
		profileAdpt = new ProfileAdaptor(m_Container.getContext(), R.id.profileListRow, R.id.profileName, strarray);
		profilelstView.setAdapter(profileAdpt);
		
		OnItemLongClickListener mLongClickHandler = new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//TODO Add Popup for EDIT/DELETE Option
				profileActionDialog(position);
				//editProfile(position);
				return false;
			}
		};
		// Create a messagehandling object as an anonymous class. 
		OnItemClickListener mMessageClickedHandler = new OnItemClickListener() 
		{     
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//view.setBackgroundColor(Color.BLUE);
				//profileAdpt.notifyDataSetChanged();
				profilelstView.setItemChecked((int) id, true);
				setProfile(ProfileNames.get((int) id));
			} 
		};  
			
		profilelstView.setOnItemClickListener(mMessageClickedHandler);
		profilelstView.setOnItemLongClickListener(mLongClickHandler);
		profilelstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	public static void setProfile( String profileName )
	{
		
		FragmentActivity act = Profile_FragTab.instance;
		
		SharedPreferences pref = act.getSharedPreferences(profileName,Context.MODE_PRIVATE);
		  
		AudioManager m_AudioManager =  (AudioManager)act.getSystemService(Context.AUDIO_SERVICE);
		WifiManager m_WifiManager = (WifiManager)act.getSystemService(Context.WIFI_SERVICE);
		Map<String, ?> allEntries = pref.getAll();
		for (Map.Entry<String, ?> key : allEntries.entrySet()) {
			//String key = entry.getKey();
			Log.i("###Message", String.valueOf(key));
			if( key.getKey().equals("volume_alarm"))
			{
				//SliderPreference volumepref = (SliderPreference) findPreference(key);
				//Log.i("###Message", String.valueOf(key.getValue()));
				m_AudioManager.setStreamVolume(AudioManager.STREAM_ALARM,  (Integer) key.getValue(), 0);
			}
			else if( key.getKey().equals("volume_media"))
			{
				Log.i("###Message", String.valueOf(key.getValue()));
				m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (Integer) key.getValue(), 0);
			}
			else if( key.getKey().equals("volume_ringer"))
			{
				Log.i("###Message", String.valueOf(key.getValue()));
				m_AudioManager.setStreamVolume(AudioManager.STREAM_RING, (Integer) key.getValue(), 0);
			}
			else if( key.getKey().equals("audio_profile"))
			{
				//ListPreference audiopref = (ListPreference)findPreference(key);
				if( key.getValue().equals("Silent" ))
				{
					m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				}
				else if( key.getValue().equals("Normal" ))
				{
					m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
				else if( key.getValue().equals("Vibrate" ))
				{
					m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				}
					
				//key.setSummary(key.getValue());
			}
			else if( key.getKey().equals("NetworkWifi"))
			{
				//CheckBoxPreference wifipref = (CheckBoxPreference)findPreference(key);
				
				m_WifiManager.setWifiEnabled((Boolean) key.getValue());
			}
			else if( key.getKey().equals("Networkdata"))
			{
				final ConnectivityManager conman = (ConnectivityManager)  act.getSystemService(Context.CONNECTIVITY_SERVICE);
			    Class conmanClass;
				try {
					conmanClass = Class.forName(conman.getClass().getName());
				
				    final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
				    connectivityManagerField.setAccessible(true);
				    final Object connectivityManager = connectivityManagerField.get(conman);
				    final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
				    final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
				    setMobileDataEnabledMethod.setAccessible(true);
			
				 //   CheckBoxPreference datapref = (CheckBoxPreference)findPreference(key);
				    boolean enabled =(Boolean) key.getValue();
				    setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			else if ( key.getKey().equals("gps"))
			{
				
			    boolean enabled = (Boolean)key.getValue();
				Context context = act;
				Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		        intent.putExtra("enabled", enabled);
		        context.sendBroadcast(intent);
			    if( enabled )
			    {
			    	

			        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			        if (! provider.contains("gps"))
			        { //if gps is disabled
			            final Intent poke = new Intent();
			            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
			            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			            poke.setData(Uri.parse("3")); 
			            context.sendBroadcast(poke);
			        }
			    }
			    else
			    {
			    	String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			        if (provider.contains("gps"))
			        { //if gps is enabled
			            final Intent poke = new Intent();
			            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			            poke.setData(Uri.parse("3")); 
			            context.sendBroadcast(poke);
			        }
			    }
			
			}
		}
		
		SettingsManager.getInstance().updateChangedProfile(profileName);
	}
	
	/*
	 * Show popup with option to Either Delete or Edit Profile
	 */
	private void profileActionDialog(final int position)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View vi = inflater.inflate(R.layout.dialog_static, null);
	    TextView edText = (TextView)vi.findViewById(R.id.dialog_textview);
	    edText.setText("Make your Selection for " + ProfileNames.get(position));
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(vi)
	    // Add action buttons
	           .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	            	   editProfile(position);
	               }
	           })
	           .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   deleteProfile(position);
	            	   dialog.cancel();
	               }
	           });      
	     builder.setTitle("Create Profile");
	     
	     
	     builder.create().show();
	}
	
	protected void deleteProfile(int position) {
		// TODO Auto-generated method stub
		if( position >= 0 && position < ProfileNames.size())
		{
			Context context= getActivity();
			SharedPreferences myPrefs = context.getSharedPreferences(ProfileNames.get(position),
			 Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = myPrefs.edit();
			editor.clear();
			editor.commit();  
			ProfileNames.remove(position);
			setProfileListUI();
		}
	}

	private void editProfile(int id)
	{
		Intent intent = new Intent();
        intent.setClass(getActivity(), SettingsActivity.class);
        intent.putExtra("profileName", ProfileNames.get(id));
        startActivity(intent);
        m_SettingMgr.updateChangedProfile(ProfileNames.get(id));
        profileAdpt.notifyDataSetChanged();
	}
	
	private class ProfileAdaptor extends ArrayAdapter<String>
	{

		public ProfileAdaptor(Context context, int resource,
				int textViewResourceId,String[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = m_Layout.inflate(R.layout.profile_list_row, parent, false);
			TextView profileName = (TextView) view.findViewById(R.id.profileName);
			profileName.setText(ProfileNames.get(position));
			convertView = view;
			if( m_SettingMgr.getCurrentProfile() == position )
			{
				convertView.setBackgroundColor(Color.GREEN);
			}
			return convertView;
		
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()){
         case R.id.btn_addprofile:
         	onAddProfile( v );
         break;
		 }
	}

	private void onAddProfile(View view) {
		// TODO Auto-generated method stub
		 // Create an instance of the dialog fragment and show it
       // DialogFragment dialog = new NoticeDialogFragment();
      //  dialog.show(getChildFragmentManager(), "NoticeDialogFragment");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View vi = inflater.inflate(R.layout.dialog_additem, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(vi)
	    // Add action buttons
	           .setPositiveButton("Create", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	            	   EditText edittxt = (EditText) vi.findViewById(R.id.dialog_Text);
	            	   ProfileNames.add(edittxt.getText().toString());
	            	   m_SettingMgr.saveProfileNames(getActivity(), ProfileNames);
	            	   setProfileListUI();
	            	   editProfile(ProfileNames.size()-1);
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   //this.getDialog().cancel();
	            	   dialog.cancel();
	               }
	           });      
	     builder.setTitle("Create Profile");
	     
	     
	     builder.create().show();

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

}
