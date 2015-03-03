package by.iharkaratkou.beans;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FinalRoutesForView {
	private List<LinkedHashMap<String, List<String>>> finalRoutesForViewValues;
	
	public void setFinalRoutesForViewValues (List<LinkedHashMap<String, List<String>>> finalRoutesForViewValues){
		this.finalRoutesForViewValues = finalRoutesForViewValues;
	}
	public List<LinkedHashMap<String, List<String>>> getFinalRoutesForViewValues (){
		return this.finalRoutesForViewValues;
	}
}
