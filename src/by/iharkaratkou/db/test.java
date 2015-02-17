package by.iharkaratkou.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import by.iharkaratkou.bsnlogic.BusinessLogicUtils;

public class test {

	public static void main(String[] args) {
		BusinessLogicUtils blu = new BusinessLogicUtils();
		ArrayList<ArrayList<String>> queryResult = null;
		//List<List<HashMap<Integer, List<String>>>> routes = new ArrayList<List<HashMap<Integer,List<String>>>>();
		List<LinkedHashMap<Integer, List<String>>> routes = new ArrayList<LinkedHashMap<Integer,List<String>>>();
		LinkedHashMap<Integer, List<String>> route = new LinkedHashMap<Integer,List<String>>();
		try {
			queryResult = blu.getLinies();
			routes = blu.getRouts(10, 9);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		for(LinkedHashMap<Integer, List<String>> routeInFor: routes){
			System.out.println(routeInFor);
		}
		
/*		for(ArrayList<String> row : queryResult){
			System.out.println(row.get(2));
			for(String cell: row){
				System.out.println(cell);
			}
		}*/
	}

}
