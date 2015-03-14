package com.example.locationtest;

import com.example.locationtest.RulesManager;

import android.os.Bundle;
import android.location.Location;

public class LocationConstraint extends ConstraintImp{

	private double mLongitude;
	private double mLatitude;
	private boolean mActivated;
	
	public LocationConstraint()
	{
		m_Type = RulesManager.LOCATION_TYPE;
		mActivated = false;
	}
	
	@Override
	public boolean Statisfied(Bundle payload) {
		// TODO Auto-generated method stub
		Location loc= payload.getParcelable("location");
		float[] results = new float[1];
		Location.distanceBetween(mLatitude, mLongitude, loc.getLatitude(), loc.getLongitude(), results);
		
		//if with in 100 meters send true
		return (results[0]<100);
	}
	public double getLongitude() {
		return mLongitude;
	}
	public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}
	public double getLatitude() {
		return mLatitude;
	}
	public void setLatitude(double d) {
		this.mLatitude = d;
	}
	

}
