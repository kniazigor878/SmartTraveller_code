package by.iharkaratkou.beans;

import java.util.ArrayList;

public class Localities {
	private ArrayList<ArrayList<String>> locValues;
	
	public void setLocValues (ArrayList<ArrayList<String>> locValues){
		this.locValues = locValues;
	}
	public ArrayList<ArrayList<String>> getLocValues (){
		return this.locValues;
	}
	
}
