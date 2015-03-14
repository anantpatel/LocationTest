package com.example.locationtest;

import java.util.StringTokenizer;

import com.example.locationtest.RulesManager;

import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.util.Log;

public class CalendarConstraint extends ConstraintImp{

	
	private String mKeyWord;
	private long mBeginTime;
	private long mEndTime;
	private static String TAG = "CalendarConstraint";
	public CalendarConstraint()
	{
		m_Type = RulesManager.CALENDAR_TYPE;
	}
	@Override
	public boolean Statisfied(Bundle payload) {
		// TODO Auto-generated method stub
//		Log.i(TAG, mBeginTime+" "+mEndTime);
//		if( (payload.getLong("beginTime") >> 0x16) == (mBeginTime >> 0x16) && (payload.getLong("endTime")>>0x16) == (mEndTime>> 0x16))
//		{
//			return true;
//		}
		boolean result = false;
		String title = payload.getString("eventName");
		StringTokenizer st = new StringTokenizer(mKeyWord);
		while (st.hasMoreElements()) {
			result =  title.contains((CharSequence) st.nextElement());
			if ( result == true )
				break;
		}
		return result;
		
	}
	public String getKeyWord() {
		return mKeyWord;
	}
	public void setKeyWord(String mKeyWord) {
		this.mKeyWord = mKeyWord;
	}
	public long getBeginTime() {
		return mBeginTime;
	}
	public void setBeginTime(long mBeginTime) {
		this.mBeginTime = mBeginTime;
	}
	public long getEndTime() {
		return mEndTime;
	}
	public void setEndTime(long mEndTime) {
		this.mEndTime = mEndTime;
	}


}
