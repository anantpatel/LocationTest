package com.example.locationtest;

public class LocationInfo {
	private String m_LocationName;
	private String m_ProfileName;
	private double m_Longitude;
	private double m_Latitude;
	
	public LocationInfo(String locationame, String profilename, double d, double e)
	{
		m_LocationName = locationame;
		m_ProfileName = profilename;
		m_Longitude = d;
		m_Latitude = e;
	}
	
	public String getLocationName()
	{
		return m_LocationName;
	}
	
	public String getProfileName()
	{
		return m_ProfileName;
	}
	
	public String getLocationValue()
	{
		return Double.toString(m_Longitude) + " " + Double.toString(m_Latitude);
	}
	
	public void setProfileName(String profilename)
	{
		m_ProfileName = profilename;
	}

	public double getLongitude()
	{
		return m_Longitude;
	
	}
	
	public double getLatitude()
	{
		return m_Latitude;
	}
}

