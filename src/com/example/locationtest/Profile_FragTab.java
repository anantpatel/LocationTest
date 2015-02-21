package com.example.locationtest;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class Profile_FragTab extends Fragment {
	
	private View m_Container;
	private ListView profilelstView;
	LayoutInflater m_Layout;	
	
	/************** Test Purpose ( Delete Later ) ********/
	static final int MAX_PROFILE = 5; 
	String [] testProfileName = {"Normal", "Slient", "LOW", "Hospital", "Movie"};

	/******* End Test Purpose***************/
	@Override
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	 View rootView = inflater.inflate(R.layout.profile_frag, container, false);
        
	 m_Container = container;
	 profilelstView = (ListView) rootView.findViewById(R.id.lv_profiles);
	 m_Layout = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //TODO::Load from appropriate Source.
	 LoadProfiles();
     //TODO::Populate the Profile Data onto the UI
	 setProfileListUI();
	 return rootView;
        
 }
	
	private void LoadProfiles()
	{
		
	}
	
	private void setProfileListUI()
	{		
		//Setup 
		String[] strarray = new String[MAX_PROFILE];
		ProfileAdaptor profileAdpt = new ProfileAdaptor(m_Container.getContext(), R.id.profileListRow, R.id.profileName, strarray);
		profilelstView.setAdapter(profileAdpt);
		
		OnItemLongClickListener mLongClickHandler = new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//TODO Add Popup for EDIT/DELETE Option
				editProfile(position);
				return false;
			}
		};
		// Create a messagehandling object as an anonymous class. 
		OnItemClickListener mMessageClickedHandler = new OnItemClickListener() 
		{     
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setProfile(position);
			} 
		};  
			
		profilelstView.setOnItemClickListener(mMessageClickedHandler); 
	}
	
	private void setProfile( int id )
	{
		// Display the fragment as the main content.
		getActivity().getFragmentManager().beginTransaction().replace(android.R.id.content,
                new TestFragmentPref()).commit();
		//Reflect all the profile setting to device
		
		//Updated Network Profile
		
		//Update Audio Profile
		
		//Update Power Profile
	}
	
	private void editProfile(int id)
	{
		//Fetch all the setting from Device
		
		//Save all the setting to device
	}
	
	private class ProfileAdaptor extends ArrayAdapter<String>
	{

		public ProfileAdaptor(Context context, int resource,
				int textViewResourceId,String[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = m_Layout.inflate(R.layout.profile_list_row, parent, false);
			TextView profileName = (TextView) view.findViewById(R.id.profileName);
			profileName.setText(testProfileName[position]);
			convertView = view;
			return convertView;
		
		}
		
		
	}

}
