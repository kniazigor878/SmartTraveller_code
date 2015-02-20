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
		List<LinkedHashMap<Integer, List<String>>> finalRoutes = new ArrayList<LinkedHashMap<Integer,List<String>>>();
		try {
			finalRoutes = blu.getRoutsByLocalities(10,9);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		for(LinkedHashMap<Integer, List<String>> routeInFor: finalRoutes){
			System.out.println(routeInFor);
		}
	}
}
