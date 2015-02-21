package com.example.locationtest;

import net.jayschwa.android.preference.SliderPreference;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

public class TestFragmentPref extends PreferenceFragment implements OnSharedPreferenceChangeListener 
{
	
	private AudioManager m_AudioManager;
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences);
	        m_AudioManager =  (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

	        	
	    }

	//Trick to removed transparency for Preference UI
	  @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);

	        getView().setBackgroundColor(Color.YELLOW);

	    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
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
			
			audiopref.setSummary(audiopref.getValue());
		}
		
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
