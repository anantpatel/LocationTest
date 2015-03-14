package com.example.locationtest;


import java.util.Calendar;
import java.util.List;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TimePicker;

public class Rules_FragTab extends Fragment implements 
View.OnClickListener
{
	
	private RulesManager mManagerofRules =  RulesManager.getInstance();
	private View m_Container;
	private ListView mRuleslstView;
	private RulezAdaptor mRulezViewAdp;
	LayoutInflater m_Layout;
	private static String TAG = "Rules_FragTab";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.rules_fragment, container, false);
		 m_Layout = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 m_Container = container;
		 mRuleslstView = (ListView) rootView.findViewById(R.id.lv_rulesView);
		 Button btn_addProfile = ( Button) rootView.findViewById(R.id.btn_addRulez);
		 btn_addProfile.setOnClickListener((OnClickListener) this);
		 
		 setRulezUI();
		 return rootView;
	}
	
	
	
	private void setRulezUI() {
		// TODO Auto-generated method stub
		
		//Setup 
		String[] strarray = new String[mManagerofRules.getRulesCount()];
		mRulezViewAdp = new RulezAdaptor(m_Container.getContext(), R.id.profileListRow, R.id.profileName, strarray);
		mRuleslstView.setAdapter(mRulezViewAdp);
		
		OnItemLongClickListener mLongClickHandler = new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				mRulesDialogAction(position);
				
				return false;
			}
		};
		
		OnItemClickListener mClikcHandler = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i(TAG, "CLICK");
				
			}
			
		};
		
		mRuleslstView.setOnItemLongClickListener(mLongClickHandler);
		mRuleslstView.setOnItemClickListener(mClikcHandler);
		mRuleslstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}



	protected void mRulesDialogAction(final int position) {
		Log.i(TAG,"onRulesDiaglog");
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View vi = inflater.inflate(R.layout.dialog_static, null);
	    TextView edText = (TextView)vi.findViewById(R.id.dialog_textview);
	    edText.setText("Make your Selection");
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(vi)
	    // Add action buttons
	           .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	            	   editRule(position);
	               }
	           })
	           .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   deleteRule(position);
	            	   dialog.cancel();
	               }
	           });      
	     builder.setTitle("Rules Action");
	     
	     
	     builder.create().show();
	}



	protected void deleteRule(int position) {
		// TODO Auto-generated method stub
		if( position >= 0 && position < mManagerofRules.getInstance().getNumberofRules())
		{
			mManagerofRules.fetchRules().remove(position);
			setRulezUI();
		}
		
	}



	protected void editRule(int position) {
		// TODO Auto-generated method stub
		
		 Intent intent = new Intent(getActivity(), RulesActivity.class);
		 intent.putExtra(RulesActivity.REQUEST_TYPE, RulesActivity.REQUEST_EDIT);
		 intent.putExtra(RulesActivity.RULE_ID, position);
		 startActivity(intent);
		
	}



	private class RulezAdaptor extends ArrayAdapter<String>
	{

		public RulezAdaptor(Context context, int resource,
				int textViewResourceId,String[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = m_Layout.inflate(R.layout.rulez_list_row, parent, false);
						
			Rules rules = mManagerofRules.getRulesAt(position);
			TextView txtView;
			//TODO::better if inserted TextView Dynamically
			txtView = (TextView) view.findViewById(R.id.ruleName);
			txtView.setText(rules.getRuleName());
			
			txtView = (TextView) view.findViewById(R.id.profilename);
			txtView.setText("Profile : "+rules.getProfile());
			
			CheckBox ckbox = (CheckBox)view.findViewById(R.id.cbActivate);
			ckbox.setChecked(rules.isActivate());
			for( int i = 0; i < rules.size(); i++)
			{
				
				if( rules.getConstraintAt(i).getType() == RulesManager.LOCATION_TYPE)
				{
					LocationConstraint lc = (LocationConstraint) rules.getConstraintAt(i);
					txtView = (TextView) view.findViewById(R.id.locationValue);
					//Long time = 
					txtView.setText( lc.getName() );
					
				}
				else
				{
					CalendarConstraint cc = (CalendarConstraint) rules.getConstraintAt(i);
					txtView = (TextView) view.findViewById(R.id.calendarValue);
					//Long time = 
					txtView.setText(cc.getKeyWord() );
				}
				
			}
			convertView = view;
			if( SettingsManager.getInstance().getCurrentRule() == position )
			{
				convertView.setBackgroundColor(Color.GREEN);
			}
			return convertView;
		
		}
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()){
         case R.id.btn_addRulez:
         	onAddRuleBtnCB( v );
         break;
		 }
	}

	@Override
	public void onResume(){
		Log.i(TAG, "Resume");
		 setRulezUI();
		super.onResume();
	}

	private void onAddRuleBtnCB(View v) {
		// TODO Auto-generated method stub
		
		//Popup Open UI to Create Rules
		 Intent intent = new Intent(getActivity(), RulesActivity.class);
		 intent.putExtra(RulesActivity.REQUEST_TYPE, RulesActivity.REQUEST_EDIT);
		 startActivity(intent);
		//TimePickerFragment diag = new TimePickerFragment();
		//diag.show(getFragmentManager(), TAG);
	}
	
	
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		Bundle bundle = (Bundle) new Bundle();
		bundle.putString("type", RulesManager.LOCATION_TYPE );
		bundle.putParcelable("location", arg0);
		mManagerofRules.checkNewEvent(bundle);
		
	}

}
