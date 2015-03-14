package com.example.locationtest;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.util.Log;

public class RulesManager {
	
	private List<Rules> m_RulesList= new ArrayList<Rules>();
	private static RulesManager m_RulesManagerInst = null;
	static String CALENDAR_TYPE = "Calendar.Type";
	static String LOCATION_TYPE = "Location.Type";
	private static String TAG = "RulesManager";
	private RulesManager()
	{
		
	}
	
	public static RulesManager getInstance()
	{
		if( m_RulesManagerInst == null)
		{
			m_RulesManagerInst = new RulesManager();
		}
		
		return m_RulesManagerInst;
	}

	//Fetch the Rules Created
	public List<Rules> fetchRules()
	{
		return m_RulesList;
	}
	
	public void addRule(Rules r)
	{
		m_RulesList.add(r);
	}
	
	//Update/Modify a Rule
	public void UpdateRule( Rules rule)
	{
		
	}

	//Fetch rules at position
	public Rules getRulesAt(int position) {
		// TODO Auto-generated method stub
		if( position < m_RulesList.size())
		{
			return m_RulesList.get(position);
		}
		return null;
	}
	
	public int getRulesCount()
	{
		if( m_RulesList != null )
		{
			return m_RulesList.size();
		}
		
		return 0;
	}

	public int getNumberofRules() {
		// TODO Auto-generated method stub
		return m_RulesList.size();
	}

	public void checkNewEvent(Bundle bundle) {
		// TODO Auto-generated method stub
		String type = bundle.getString("type", "");
		
		for(int i = 0; i < m_RulesList.size();i++)
		{
			boolean result = true;
			boolean found = false;
			Rules rule = m_RulesList.get(i);
			Log.i(TAG, rule.getProfile());
			if( rule.isActivate() == true)
			{
				for( int j = 0; j < rule.size();j++ )
				{
					if( type == rule.getConstraintAt(j).getType())
					{
						found = rule.getConstraintAt(j).Statisfied(bundle);
						
					}
					result = result & found;
				}
				
				
			}
			
			if( result == true && rule.isActivate() == true)
			{
				//found the match
				
				String Profile = rule.getProfile();
				Log.i(TAG, "Is is a MAtCH " + Profile);
				int id = SettingsManager.getInstance().getProfileByid(Profile);
				SettingsManager.getInstance().setCurrentRule(id);
				Profile_FragTab.setProfile(Profile);
				break;
			}
	
			
		}
	}
}
