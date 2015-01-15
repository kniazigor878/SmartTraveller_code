package by.iharkaratkou.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {

	public static void main(String[] args) {
		DatabaseUtils dbu = new DatabaseUtils();
		ArrayList<ArrayList<String>> queryResult = null;
		List<List<HashMap<Integer, List<String>>>> routes = new ArrayList<List<HashMap<Integer,List<String>>>>();
		List<HashMap<Integer, List<String>>> route = new ArrayList<HashMap<Integer,List<String>>>();
		HashMap<Integer, List<String>> routeLinie = new HashMap<Integer,List<String>>();
		try {
			queryResult = dbu.getLinies();
			routes = dbu.getRouts(10, 9);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(routes);
		
/*		for(ArrayList<String> row : queryResult){
			System.out.println(row.get(2));
			for(String cell: row){
				System.out.println(cell);
			}
		}*/
	}

}
