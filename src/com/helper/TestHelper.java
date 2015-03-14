package com.helper;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.example.locationtest.CalendarConstraint;
import com.example.locationtest.Rules;
import com.example.locationtest.RulesManager;
import com.example.locationtest.SettingsManager;


public class TestHelper {
	
	private static String TAG = "TestHelper";
	public static void createTestForRules(Context context)
	{
		
		
		if (RulesManager.getInstance().getNumberofRules() == 0 )
		{
			//Create dummy Rules for Testing purpose
			long currentTime = System.currentTimeMillis();
			
			currentTime += 2*60*1000;
			Log.i(TAG, Long.toString(currentTime));
			long end = currentTime + 60*60*1000;
			
			long start2 = end + 40*60*1000;
			long end2 = start2 + 40*60*1000;
			
			Object[] rulesinfo = { "Work", true, currentTime,end,  "Meeting","Normal",
								   "Gym", false, start2, end2,"workout","Loud"};
			int index = 0;
			for( int i = 0; i < 2; i++ )
			{
				CalendarConstraint c = new CalendarConstraint();
				c.setName((String) rulesinfo[index++]);
				boolean enabled = (Boolean) (rulesinfo[index++]);
				c.setBeginTime((Long) (rulesinfo[index++]));
				c.setEndTime((Long)(rulesinfo[index++]));
				c.setKeyWord((String)(rulesinfo[index++]));
				
				
				Rules r = new Rules();
				r.addConstraint(c);
				r.setRuleName(c.getName());
				r.setProfile((String)rulesinfo[index++]);
				r.Activate(enabled);
				RulesManager.getInstance().addRule(r);
			}
		}
		
		
		saveRulesToPref(context);
		List<Rules> rl = SettingsManager.getInstance().getRuleList(context);
		List<Rules> rgold = RulesManager.getInstance().fetchRules();
		for( int i = 0; i < rl.size(); i ++)
		{
			if(rl.get(i).getProfile() == rgold.get(i).getProfile())
			{
				Log.i(TAG, "Error");
			}
		
		}
		
	}
	public static void saveRulesToPref(Context applicationContext) {
		// TODO Auto-generated method stub
		SettingsManager.getInstance().saveRuleList(applicationContext, RulesManager.getInstance().fetchRules());
	}

}
