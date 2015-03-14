package com.example.locationtest;



import java.util.Calendar;
import java.util.List;

import com.google.android.gms.internal.fm;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter.LengthFilter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RulesActivity extends Activity {
	private static String TAG = "RulesActivity";
	public static String REQUEST_CREATE = "Create";
	public static String REQUEST_EDIT = "Edit";
	public static String REQUEST_TYPE = "Type";
	public static String RULE_ID = "Id";
	private static int INVALID_ID = -1;
	private TimePickerUI mFromTime;
	private TimePickerUI mToTime;
	private TimePickerDialog td;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_rules);
		Intent i = getIntent();
		if( REQUEST_EDIT ==  i.getStringExtra(RulesActivity.REQUEST_TYPE))
		{
			generateUIforEdit(i.getIntExtra(RULE_ID,INVALID_ID));
		}
		else//if( REQUEST_CREATE ==  i.getStringExtra(RulesActivity.REQUEST_TYPE))
		{
			updateCreateRuleUI();
		}
	}

	private void generateUIforEdit(int ruleId) {
		// TODO Auto-generated method stub
		if( INVALID_ID == ruleId)
		{
			Rules rule = RulesManager.getInstance().getRulesAt(ruleId);
			
			//Set RuleName
			TextView txtView = (TextView)findViewById(R.id.ruleName);
			txtView.setText(rule.getRuleName());
			
			//SetProfile
			setProfileSpinner();
			Spinner spinner = (Spinner) findViewById(R.id.profile_spinner);
			List<String> profilelist= SettingsManager.getInstance().getProfileNames(getApplicationContext());
			spinner.setSelection(profilelist.indexOf(rule.getProfile()));
			
					
			//Set Contraint
			for( int i = 0; i < rule.size(); i++)
			{
				
				if( rule.getConstraintAt(i).getType() == RulesManager.LOCATION_TYPE)
				{
					LocationConstraint lc = (LocationConstraint) rule.getConstraintAt(i);
					//Initiate Location
					setLocationSpinner(lc.getName());
					
				}
				else
				{
					CalendarConstraint cc = (CalendarConstraint) rule.getConstraintAt(i);
					txtView = (TextView) findViewById(R.id.keyword);
					//Long time = 
					txtView.setText(cc.getKeyWord() );
				}
				
			}
			
		}
	}

	private void updateCreateRuleUI() {
		
		setProfileSpinner();
		setLocationSpinner(null);
		setTimedata(false);
		
	}

	
	private void setLocationSpinner(String selector) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Spinner spinner = (Spinner) findViewById(R.id.location_spinner);
				// Create an ArrayAdapter using the string array and a default spinner layout
				ArrayAdapter <CharSequence> adapter =
						  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						 
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				List<LocationInfo> locaitonList= SettingsManager.getInstance().getLocationProfileMapping(getApplicationContext());
				adapter.add("None");
				for(int i = 0; i < locaitonList.size(); i++)
				{
					adapter.add(locaitonList.get(i).getLocationName());
					if( selector != null )
					{
						if(selector == locaitonList.get(i).getLocationName())
						{
							spinner.setSelection(i);
						}
					}
				}
				// Apply the adapter to the spinner
				spinner.setAdapter(adapter);
		
	}

	private void setProfileSpinner() {
		// TODO Auto-generated method stub
		
				Spinner spinner = (Spinner) findViewById(R.id.profile_spinner);
				// Create an ArrayAdapter using the string array and a default spinner layout
				ArrayAdapter <CharSequence> adapter =
						  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						 
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				List<String> profilelist= SettingsManager.getInstance().getProfileNames(getApplicationContext());
				adapter.add("None");
				for(int i = 0; i < profilelist.size(); i++)
				{
					adapter.add(profilelist.get(i));
				}
				// Apply the adapter to the spinner
				spinner.setAdapter(adapter);
	}

	public void onbtnCancelCB(View v)
	{
		super.finish();
	}
	
	public void onbtnSaveCB(View v)
	{
		Spinner spinner = (Spinner) findViewById(R.id.location_spinner);
		String text = spinner.getSelectedItem().toString();
		int locationid = (int) spinner.getSelectedItemId()-1;
		EditText ruleName =(EditText) findViewById(R.id.txt_rulename);
		boolean bruleNameValid = !ruleName.getText().toString().isEmpty();
		
		EditText keyword =(EditText) findViewById(R.id.keyword);
		boolean keywordValid = !keyword.getText().toString().isEmpty();
		
		
		//Error Message
		if( !bruleNameValid)
			ruleName.setError("Rule Name Empty");
		
		boolean bConstraintValid = !keywordValid && text.equals("None");
		if(bConstraintValid)//todO pattern
			Toast.makeText(getApplicationContext(), "Either fill Location or Keyword", Toast.LENGTH_LONG).show();
		
		if( bruleNameValid && !bConstraintValid)
		{
			Rules r = new Rules();
			//TODO save to RuleManager
						
			if(keywordValid)
			{
				CalendarConstraint c = new CalendarConstraint();
				c.setKeyWord(keyword.getText().toString());
				r.addConstraint(c);
			}
			else
			{
				List<LocationInfo> loclist= SettingsManager.getInstance().getLocationProfileMapping(getApplicationContext());
				if( loclist != null )
				{
					LocationConstraint l = new LocationConstraint();
					l.setName(text);
					l.setLatitude(loclist.get(locationid).getLatitude());
					l.setLongitude(loclist.get(locationid).getLongitude());
					r.addConstraint(l);
					
					//For Battery optmization, check location only if Loocation is added.
				}
			}
			
			spinner = (Spinner) findViewById(R.id.profile_spinner);
			r.setProfile(spinner.getSelectedItem().toString());
			
			r.setRuleName(ruleName.getText().toString());
			RulesManager.getInstance().addRule(r);
			
			super.finish();
		}
		
	}
	
	
	
	public void onbtnTimeCB(View v)
	{
		
		/*final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		new TimePickerDialog(this, null, hour, minute, false).show();*/
		
		mFromTime.show();
	}
	
	private void setTimedata(boolean create) {
		// TODO Auto-generated method stub
		final Button btnfromtime = (Button)findViewById(R.id.btnfromtime);
		final Button btntotime = (Button)findViewById(R.id.btntotime);
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		
		btnfromtime.setText( String.valueOf(hour)+":"+String.valueOf(minute));
		btntotime.setText( String.valueOf(hour+1)+":"+String.valueOf(minute));
		TimePickerDialog.OnTimeSetListener fromtimeListner = new TimePickerDialog.OnTimeSetListener(){

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				btnfromtime.setText( String.valueOf(hourOfDay)+":"+String.valueOf(minute));
				mFromTime.setHourOfDay(hourOfDay);
				mFromTime.setMinute(minute);
			}
			
		};
		
		TimePickerDialog.OnTimeSetListener totimeListner = new TimePickerDialog.OnTimeSetListener(){

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				btntotime.setText( String.valueOf(hourOfDay)+":"+String.valueOf(minute));
				mToTime.setHourOfDay(hourOfDay);
				mToTime.setMinute(minute);
				 
			}
			
		};
				
		mFromTime = new TimePickerUI(this, 1,fromtimeListner, hour,minute, false, btnfromtime);
		mToTime = new TimePickerUI(this, 1,totimeListner, hour+1,minute, false, btnfromtime);
	}
	public void onbtnDateCB(View v)
	{
	
	}
	
	
	public static class TimePickerUI extends TimePickerDialog
	implements TimePickerDialog.OnTimeSetListener
	{
		private int mHourOfDay;
		private int mMinute;
		private View parent;
		public TimePickerUI(Context context, int theme,
				OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView, View v) {
			super(context, theme, callBack, hourOfDay, minute, is24HourView);
			parent = v;
			
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHourOfDay = hourOfDay;
			mMinute = minute;
			Button btn = (Button)parent;
			btn.setText( String.valueOf(hourOfDay)+":"+String.valueOf(minute));
			  
		}

		public int getHourOfDay() {
			return mHourOfDay;
		}

		public void setHourOfDay(int mHourOfDay) {
			this.mHourOfDay = mHourOfDay;
		}

		public int getMinute() {
			return mMinute;
		}

		public void setMinute(int mMinute) {
			this.mMinute = mMinute;
		}
		
	}
	
	public static class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {
		
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// Use the current time as the default values for the picker
				final Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				
				// Create a new instance of TimePickerDialog and return it
				return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
			}
			
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
				Log.i(TAG, String.valueOf(hourOfDay)+ " "+ String.valueOf(minute));
			}
		}	
}
