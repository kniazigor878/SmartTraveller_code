package by.iharkaratkou.db;

import java.sql.*;
import java.util.*;

public class DatabaseUtils {
	
	Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		String login = "ihar";
		String pass = "ihar";
		conn = DriverManager.getConnection("jdbc:mysql://localhost/test",login,pass);
		return conn;
	}
	
	ArrayList<ArrayList<String>> execSelect(String query) throws SQLException, ClassNotFoundException{	
		Connection conn = getConnection();
		ArrayList<ArrayList<String>> queryResult = new ArrayList<ArrayList<String>>();
		ArrayList<String> queryResultRow = new ArrayList<String>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		ResultSetMetaData meta = rs.getMetaData();
		int colCount = meta.getColumnCount();
		while(rs.next()){
			for (int column = 1; column <= colCount; column++){
				queryResultRow.add(rs.getString(column));
			}
			queryResult.add((ArrayList<String>) queryResultRow.clone());
			queryResultRow.clear();
		}
		return queryResult;
	}
	
	ArrayList<ArrayList<String>> getLinies() throws ClassNotFoundException, SQLException{
		String query = "SELECT * FROM linies";
		return execSelect(query);
	}
	
		
	List<List<HashMap<Integer, List<String>>>> getRouts(Integer station_start, Integer station_end) throws ClassNotFoundException, SQLException{
		List<List<HashMap<Integer, List<String>>>> routes = new ArrayList<List<HashMap<Integer,List<String>>>>();
		List<HashMap<Integer, List<String>>> route = new ArrayList<HashMap<Integer,List<String>>>();
		HashMap<Integer, List<String>> routeLinie = new HashMap<Integer,List<String>>();
		ArrayList<ArrayList<String>> linies = getLinies();
		final Integer linies_desc_id = 1;
		final Integer station_id = 2;
		List<String> tempList_origin = new ArrayList<String>();
		List<String> tempList_clon = new ArrayList<String>();
		
		for(ArrayList<String> row : linies){
			if (Integer.parseInt(row.get(station_id)) == station_start){
				tempList_origin.add(row.get(station_id));
				tempList_clon = (List<String>) ((ArrayList) tempList_origin).clone();
				routeLinie.put(Integer.parseInt(row.get(linies_desc_id)), tempList_clon);
				route.add((HashMap<Integer, List<String>>) routeLinie.clone());
				routes.add(this.cloneListHm(route));
				tempList_origin.clear();
				routeLinie.clear();
				route.clear();
			}
			//System.out.println(row.get(2));
		}
		
		return routes;
	}
	
	static List<HashMap<Integer, List<String>>> cloneListHm(List<HashMap<Integer, List<String>>> list) {
		List<HashMap<Integer, List<String>>> clone = new ArrayList<HashMap<Integer, List<String>>>(list.size());
	    for(HashMap<Integer, List<String>> item: list) clone.add((HashMap<Integer, List<String>>) item.clone());
	    return clone;
	}
	
/*	static List<String> cloneListString(List<String> list) {
	    List<String> clone = new ArrayList<String>(list.size());
	    for(String item: list) clone.add((String) item.clone());
	    return clone;
	}*/
	
}
