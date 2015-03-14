package com.example.locationtest;

import java.util.ArrayList;
import java.util.List;

public class Rules {
	
	private List<ConstraintImp> ConstraintList = new ArrayList<ConstraintImp>();
	private boolean m_bActivated;
	private String m_RuleName;
	private String m_Profile;
	public Rules()
	{
		m_bActivated = true;
	}
	
	public void Activate(boolean activate)
	{
		m_bActivated = activate;
	}
	
	public boolean isActivate()
	{
		return m_bActivated;
	}
	public List<ConstraintImp> getContraints()
	{
		return ConstraintList;
	}
	
	
	public void addConstraint(ConstraintImp constraint)
	{
		ConstraintList.add(constraint);
	}
	
	public ConstraintImp getConstraintAt(int i) {
		// TODO Auto-generated method stub
		if( i < ConstraintList.size())
		{
			return ConstraintList.get(i);
		}
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		
		return ConstraintList.size();
	}

	public void setProfile(String profile) {
		// TODO Auto-generated method stub
		m_Profile = profile;
	}
	
	public String getProfile()
	{
		return m_Profile;
	}

	public String getRuleName() {
		return m_RuleName;
	}

	public void setRuleName(String m_RuleName) {
		this.m_RuleName = m_RuleName;
	}
}

