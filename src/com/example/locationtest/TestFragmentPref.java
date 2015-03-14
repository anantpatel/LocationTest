package com.example.locationtest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.gson.Gson;

import net.jayschwa.android.preference.SliderPreference;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

public class TestFragmentPref extends PreferenceFragment implements OnSharedPreferenceChangeListener 
{
	
	private String m_ProfileName;
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	         PreferenceManager prefMgr = getPreferenceManager();
	         m_ProfileName = getArguments().getString("profile"); 
	         Log.i("TestFrag", m_ProfileName);
	         prefMgr.setSharedPreferencesName(m_ProfileName);
	         prefMgr.setSharedPreferencesMode(Context.MODE_PRIVATE);


	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences);
	        
	      
	        	
	    }

	//Trick to removed transparency for Preference UI
	  @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        getView().setBackgroundColor(Color.rgb(0xFF, 0xF6, 0xCE));

	    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		AudioManager m_AudioManager =  (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
		WifiManager m_WifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
		
	    Log.i("###Message",key);
		if( key.equals("volume_alarm"))
		{
			SliderPreference volumepref = (SliderPreference) findPreference(key);
			Log.i("###Message", String.valueOf(volumepref.getValue()));
			m_AudioManager.setStreamVolume(AudioManager.STREAM_ALARM, (int) volumepref.getValue(), 0);
		}
		else if( key.equals("volume_media"))
		{
			SliderPreference volumepref = (SliderPreference) findPreference(key);
			Log.i("###Message", String.valueOf(volumepref.getValue()));
			m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volumepref.getValue(), 0);
		}
		else if( key.equals("volume_ringer"))
		{
			SliderPreference volumepref = (SliderPreference) findPreference(key);
			Log.i("###Message", String.valueOf(volumepref.getValue()));
			m_AudioManager.setStreamVolume(AudioManager.STREAM_RING, (int) volumepref.getValue(), 0);
		}
		else if( key.equals("audio_profile"))
		{
			ListPreference audiopref = (ListPreference)findPreference(key);
			if( audiopref.getValue().equals("Silent" ))
			{
				m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
			else if( audiopref.getValue().equals("Normal" ))
			{
				m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
			else if( audiopref.getValue().equals("Vibrate" ))
			{
				m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			}
				
			audiopref.setSummary(audiopref.getValue());
		}
		else if( key.equals("NetworkWifi"))
		{
			CheckBoxPreference wifipref = (CheckBoxPreference)findPreference(key);
			boolean enabled = wifipref.isChecked();
			m_WifiManager.setWifiEnabled(enabled);
		}
		else if( key.equals("Networkdata"))
		{
			final ConnectivityManager conman = (ConnectivityManager)  getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		    Class conmanClass;
			try {
				conmanClass = Class.forName(conman.getClass().getName());
			
			    final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
			    connectivityManagerField.setAccessible(true);
			    final Object connectivityManager = connectivityManagerField.get(conman);
			    final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
			    final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			    setMobileDataEnabledMethod.setAccessible(true);
	
			    CheckBoxPreference datapref = (CheckBoxPreference)findPreference(key);
			    boolean enabled = datapref.isChecked();
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
		else if ( key.equals("gps"))
		{
			CheckBoxPreference gpspref = (CheckBoxPreference)findPreference(key);
		    boolean enabled = gpspref.isChecked();
			Context context = getActivity();
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
		//SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(m_Container.getContext());
		Context context = getActivity();
		SharedPreferences sharedPref = context.getSharedPreferences(
				m_ProfileName, Context.MODE_PRIVATE);
		
		Gson gson = new Gson();
		String shared = gson.toJson(sharedPref);
		

	}

	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();
	}
	
}
