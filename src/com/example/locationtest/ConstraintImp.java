package com.example.locationtest;

import android.os.Bundle;

public class ConstraintImp {
	
	private String m_ConstraintName;
	protected String m_Type;
	 
	
	//Return Name of the Contraint
	public String getName()
	{
		return m_ConstraintName;
	}
	
	//Set the Name of the Constraint
	public void setName(String name)
	{
		m_ConstraintName = name;
	}
	
	//Return Type of the Constraint
	public String getType()
	{
		return m_Type;
	}
	
		
	//Check if the condition satisfied the Contraint
	public boolean Statisfied(Bundle payload)
	{
		return false;
	}
	
	

}
