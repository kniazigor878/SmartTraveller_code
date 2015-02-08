package by.iharkaratkou.db;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

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
	
		
	List<TreeMap<Integer, List<String>>> getRouts(Integer station_start, Integer station_end) throws ClassNotFoundException, SQLException{
		//List<List<HashMap<Integer, List<String>>>> routes = new ArrayList<List<HashMap<Integer,List<String>>>>();
		List<TreeMap<Integer, List<String>>> routes = new ArrayList<TreeMap<Integer,List<String>>>();
		TreeMap<Integer, List<String>> route = new TreeMap<Integer,List<String>>();
		ArrayList<ArrayList<String>> linies = getLinies();
		final Integer LINIES_DESC_ID = 1;
		final Integer STATION_ID = 2;
		List<String> tempList_origin = new ArrayList<String>();
		List<String> tempList_clon = new ArrayList<String>();
		
		for(ArrayList<String> row : linies){
			if (Integer.parseInt(row.get(STATION_ID)) == station_start){
				tempList_origin.add(row.get(STATION_ID));
				tempList_clon = (List<String>) ((ArrayList) tempList_origin).clone();
				route.put(Integer.parseInt(row.get(LINIES_DESC_ID)), tempList_clon);
				routes.add((TreeMap<Integer, List<String>>) route.clone());
				//routes.add(this.cloneListHm(route));
				tempList_origin.clear();
				route.clear();
				//routes.clear();
			}
			//System.out.println(row.get(2));
		}
		makeIteration(routes,linies);
		makeIteration(routes,linies);
		makeIteration(routes,linies);
		return routes;
	}
	
	static List<TreeMap<Integer, List<String>>> cloneListHm(List<TreeMap<Integer, List<String>>> list) {
		List<TreeMap<Integer, List<String>>> clone = new ArrayList<TreeMap<Integer, List<String>>>(list.size());
	    for(TreeMap<Integer, List<String>> item: list) clone.add((TreeMap<Integer, List<String>>) item.clone());
		return clone;
	}
	
	/**
	 * This method makes a "deep clone" of any Java object it is given.
	 */
	Object deepClone(Object object) {
	   try {
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(object);
	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	     ObjectInputStream ois = new ObjectInputStream(bais);
	     return ois.readObject();
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	     return null;
	   }
	 }
	
	List<TreeMap<Integer, List<String>>> makeIteration(List<TreeMap<Integer, List<String>>> routes, ArrayList<ArrayList<String>> linies) {
		//choose last elements in HashMaps
		boolean[] isStationUpChanged = new boolean[routes.size()];;
		List<TreeMap<Integer, List<String>>> tempRoutes = (List<TreeMap<Integer, List<String>>>) deepClone(routes);
		String[] arrayStationDown = new String[routes.size()];
		int hmCounter = 0;
		for (TreeMap<Integer, List<String>> hm : routes) {
			Entry<Integer, List<String>> lastEntry = hm.lastEntry();
			//choose last elements in List
			Integer listSize = lastEntry.getValue().size();
			System.out.println(lastEntry.getKey());
			System.out.println(lastEntry.getValue().get(listSize - 1));
			Integer lastKey = lastEntry.getKey();
			String lastListValue = lastEntry.getValue().get(listSize - 1);
			System.out.println("Station Up: " + getStation(linies,lastKey,lastListValue,"Up"));
			System.out.println("Station Down: " + getStation(linies,lastKey,lastListValue,"Down"));
			String stationUp = getStation(linies,lastKey,lastListValue,"Up");
			String stationDown = getStation(linies,lastKey,lastListValue,"Down");
			TreeMap<Integer, List<String>> hmWithNewLinieStations = null;
			if(stationUp != ""){
				//TreeMap<Integer, List<String>> hmTemp = (TreeMap<Integer, List<String>>) deepClone(hm);
				hmWithNewLinieStations = addNewLinieStations(hm,stationUp);
				/*if(!hmWithNewLinieStations.equals(hmTemp)){
					isStationUpChanged[hmCounter] = true;
				}else{
					isStationUpChanged[hmCounter] = false;
				}*/
				System.out.println("routes: " + routes);
			}
			//System.out.println("add in hm: " + hmWithNewLinieStations);
			//System.out.println("hmCounter: " + hmCounter);
			//System.out.println("stationDown: " + stationDown);
			arrayStationDown[hmCounter] = stationDown;
			hmCounter++;
			//make iteration:
			//1) in one direction in linie
			//2) in other direction in linie
			//3) change linie
			//4) change station in current local
		}
		
		/*hmCounter = 0;
		for(TreeMap<Integer, List<String>> hm: tempRoutes){
			boolean isStationAlreadyExist = false;
			for(List<String> valueList: hm.values()){
				for(String value: valueList){
					if(value.equals(arrayStationDown[hmCounter])){
						isStationAlreadyExist = true;
					}
				}
			}
			if(arrayStationDown[hmCounter] != "" && !isStationAlreadyExist){
				if(isStationUpChanged[hmCounter]){
					System.out.println("isStationUpChanged1: " + isStationUpChanged[hmCounter]);
					
					Entry<Integer, List<String>> lastEntry = hm.lastEntry();
					lastEntry.getValue().add(arrayStationDown[hmCounter]);
					routes.add(hm);
				}else{
					System.out.println("isStationUpChanged2: " + isStationUpChanged[hmCounter]);
					System.out.println("hm: " + hm);
					System.out.println("arrayStationDown[hmCounter]: " + arrayStationDown[hmCounter]);
					TreeMap<Integer, List<String>> hmWithNewLinieStations = addNewLinieStations(routes.get(routes.lastIndexOf(hm)),arrayStationDown[hmCounter]);
				}
			}
			
			hmCounter++;
		}*/
		
		System.out.println("routes: " + routes);
		
		
		return routes;
		
	}
	
	String getStation(ArrayList<ArrayList<String>> linies, Integer linie, String station, String direction){
		String stationNext = "";
		Double sign = (double) (direction.equals("Up") ? 1 : -1);
		Integer stationSequenceDown = (int) (getSeqByLinieAndStation(linies, linie, station) + sign);
		for(ArrayList<String> row : linies){
			if(row.get(1).equals(linie.toString()) && row.get(3).equals(stationSequenceDown.toString())){
				stationNext = row.get(2);
			}
		}
		return stationNext;
	}
	
	Integer getSeqByLinieAndStation(ArrayList<ArrayList<String>> linies, Integer linie, String station){
		Integer stationSequence = null;
		for(ArrayList<String> row : linies){
			if(row.get(1).equals(linie.toString()) && row.get(2).equals(station)){
				stationSequence = Integer.parseInt(row.get(3));
			}
		}
		return stationSequence;
	}
	
	TreeMap<Integer, List<String>> addNewLinieStations(TreeMap<Integer, List<String>> hm, String stationUp){
		boolean isStationAlreadyExist = false;
		for(List<String> valueList: hm.values()){
			for(String value: valueList){
				if(value.equals(stationUp)){
					isStationAlreadyExist = true;
				}
			}
		}
		
		if(!isStationAlreadyExist){
			Entry<Integer, List<String>> lastEntry = hm.lastEntry();
			lastEntry.getValue().add(stationUp);
		}
		return hm;
	}
	
/*	static List<String> cloneListString(List<String> list) {
	    List<String> clone = new ArrayList<String>(list.size());
	    for(String item: list) clone.add((String) item.clone());
	    return clone;
	}*/
	
}
