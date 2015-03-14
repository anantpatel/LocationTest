package com.example.locationtest;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.internal.mc;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SettingsManager {
	
	 private static SettingsManager instance = null;
	 private int CurrentProfile;
	 private int CurrentLocation = 0;
	 private int CurrentRule = -1;
	 private Context m_Context;
	 //Constants
	 private static String PROFILE_NAMES = "ProfileNames";
	 private static String PROFILE_LIST = "ProfileList";
	 static final String PREF_LOCATIONMAP = "LocationProfileMap";
	 private static String LOCATION_LIST = "LocationProfileList";
	 private static String RULES_MAP = "RuleMap";
	 private static String RULES_LIST = "RuleList";
	 
	 public static String BATTERY_OPT_MODE = "Battery_Saver";
	 private boolean BatteryOptimizedMode = true;
		
	 public Context getContext() {
		return m_Context;
	}

	public void setContext(Context context) {
		m_Context = context;
	}

	public void setCurrentRule( int id )
	{
		CurrentRule = id;
	}
	
	public int getCurrentRule()
	{
		return CurrentRule;
	}
	
	public int getProfileByid(String profile)
	{
		return getProfileNames(m_Context).indexOf(profile);
		
	}
	
	protected SettingsManager() {
	      // Exists only to defeat instantiation.
	   }
	
	   public static SettingsManager getInstance() {
	      if(instance == null) {
	         instance = new SettingsManager();
	      }
	      return instance;
	   }
	   
	   
	   
	   public List<String> getProfileNames(Context context)
	   {
		   SharedPreferences mPrefs = context.getSharedPreferences(PROFILE_NAMES, 
					Context.MODE_PRIVATE);
					
			Type listType = new TypeToken<List<String>>() {}.getType();
			Gson gson = new Gson();
			String json = mPrefs.getString(PROFILE_LIST, "");
		    return gson.fromJson(json, listType);
	   }
	   
	   public void saveProfileNames( Context context, List<String> infolist)
	   {
		   SharedPreferences mPrefs = context.getSharedPreferences(PROFILE_NAMES, 
					Context.MODE_PRIVATE);
			Editor prefsEditor = mPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(infolist);
			prefsEditor.putString(PROFILE_LIST, json);
			prefsEditor.commit();
	   }
	   
	   public List<LocationInfo> getLocationProfileMapping(Context context)
	   {
		   SharedPreferences mPrefs = context.getSharedPreferences(PREF_LOCATIONMAP, 
					Context.MODE_PRIVATE);
					
			Type listType = new TypeToken<List<LocationInfo>>() {}.getType();
			Gson gson = new Gson();
			String json = mPrefs.getString(LOCATION_LIST, "");
		    return gson.fromJson(json, listType);
	   }
	   
	   public void saveLocationProfileMapping( Context context, List<LocationInfo> infolist)
	   {
		   SharedPreferences mPrefs = context.getSharedPreferences(PREF_LOCATIONMAP, 
					Context.MODE_PRIVATE);
			Editor prefsEditor = mPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(infolist);
			prefsEditor.putString(LOCATION_LIST, json);
			prefsEditor.commit();
	   }

	public void updateChangedProfile(String profileName) {
		
		
		// TODO Update Current Location with this new profile
		List<String> profileNameLst = getProfileNames(m_Context);
		CurrentProfile = profileNameLst.indexOf(profileName);
		List<LocationInfo> locmap = getLocationProfileMapping(m_Context);
		
		if( locmap.size() > 0 )
		{
			locmap.get(CurrentLocation).setProfileName(profileName);
			saveLocationProfileMapping(m_Context, locmap);
		}
		
		
	}
	
	public int getCurrentProfile()
	{
		return CurrentProfile;
	
	}
	
	public void saveRuleList(Context context, List<Rules> ruleList)
	{
		 SharedPreferences mPrefs = context.getSharedPreferences(RULES_MAP, 
					Context.MODE_PRIVATE);
					
		 Editor prefsEditor = mPrefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(ruleList);
		prefsEditor.putString(RULES_LIST, json);
		prefsEditor.commit();
	}
	
   public List<Rules> getRuleList(Context context)
   {
	   SharedPreferences mPrefs = context.getSharedPreferences(RULES_MAP, 
				Context.MODE_PRIVATE);
				
		Type listType = new TypeToken<List<Rules>>() {}.getType();
		Gson gson = new Gson();
		String json = mPrefs.getString(RULES_LIST, "");
	    return gson.fromJson(json, listType);
   }
   
   public void EnableBatteryOptimizedMode(boolean status)
   {
	   BatteryOptimizedMode = status;
   }
}
