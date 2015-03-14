package com.example.locationtest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.jayschwa.android.preference.SliderPreference;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;



public class SettingsActivity extends Activity {
	
	

	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Intent intent = getIntent();
	        String profile = intent.getStringExtra("profileName");
	        PreferenceFragment pref = new TestFragmentPref();
	        Bundle bundle = new Bundle();
	        bundle.putString("profile", profile);
	        pref.setArguments(bundle);
	        getFragmentManager().beginTransaction().replace(android.R.id.content,
	               pref ).commit();
	  }
	  
	  public void setProfileToDevice(String ProfileName)
	  {
//			SharedPreferences pref = this.getSharedPreferences(ProfileName,Context.MODE_PRIVATE);
//			  
//			AudioManager m_AudioManager =  (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//			WifiManager m_WifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//			Map<String, ?> allEntries = pref.getAll();
//			for (Map.Entry<String, ?> key : allEntries.entrySet()) {
//				Log.i("###Message", String.valueOf(key.getValue()));
//				if( key.equals("volume_alarm"))
//				{
//					//SliderPreference volumepref = (SliderPreference) findPreference(key);
//					//Log.i("###Message", String.valueOf(key.getValue()));
//					m_AudioManager.setStreamVolume(AudioManager.STREAM_ALARM,  (Integer) key.getValue(), 0);
//				}
//				else if( key.equals("volume_media"))
//				{
//					Log.i("###Message", String.valueOf(key.getValue()));
//					m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (Integer) key.getValue(), 0);
//				}
//				else if( key.equals("volume_ringer"))
//				{
//					Log.i("###Message", String.valueOf(key.getValue()));
//					m_AudioManager.setStreamVolume(AudioManager.STREAM_RING, (Integer) key.getValue(), 0);
//				}
//				else if( key.equals("audio_profile"))
//				{
//					//ListPreference audiopref = (ListPreference)findPreference(key);
//					if( key.getValue().equals("Silent" ))
//					{
//						m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//					}
//					else if( key.getValue().equals("Normal" ))
//					{
//						m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//					}
//					else if( key.getValue().equals("Vibrate" ))
//					{
//						m_AudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//					}
//						
//					//key.setSummary(key.getValue());
//				}
//				else if( key.equals("NetworkWifi"))
//				{
//					//CheckBoxPreference wifipref = (CheckBoxPreference)findPreference(key);
//					
//					m_WifiManager.setWifiEnabled(!m_WifiManager.isWifiEnabled());
//				}
//				else if( key.equals("Networkdata"))
//				{
//					final ConnectivityManager conman = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);
//				    Class conmanClass;
//					try {
//						conmanClass = Class.forName(conman.getClass().getName());
//					
//					    final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
//					    connectivityManagerField.setAccessible(true);
//					    final Object connectivityManager = connectivityManagerField.get(conman);
//					    final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
//					    final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//					    setMobileDataEnabledMethod.setAccessible(true);
//				
//					 //   CheckBoxPreference datapref = (CheckBoxPreference)findPreference(key);
//					    boolean enabled =(Boolean) key.getValue();
//					    setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalArgumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (InvocationTargetException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (NoSuchMethodException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (NoSuchFieldException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//				}
//				
//				
//				
//			}
	  }
}
