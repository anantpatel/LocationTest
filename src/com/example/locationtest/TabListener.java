package com.example.locationtest;

import android.support.v7.app.ActionBar;

//import android.app.Fragment;
//import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;


public class TabListener implements ActionBar.TabListener{

	Fragment fragment;
	
	public TabListener(Fragment fragTab1) {
		// TODO Auto-generated constructor stub
		this.fragment = fragTab1;
	}

	@Override
	public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0,
			FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		fragment.onResume();
	}

	@Override
	public void onTabSelected(android.support.v7.app.ActionBar.Tab arg0,
			FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		arg1.replace(R.id.fragment_container, (Fragment)fragment);
	}

	@Override
	public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0,
			FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

		

}
