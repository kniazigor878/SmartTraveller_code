package by.iharkaratkou.bsnlogic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import by.iharkaratkou.*;
import by.iharkaratkou.db.DatabaseUtils;
import by.iharkaratkou.javautils.JavaHelpUtils;


public class BusinessLogicUtils {
	
	public ArrayList<ArrayList<String>> getLinies() throws ClassNotFoundException, SQLException{
		DatabaseUtils dbu = new DatabaseUtils();
		String query = "SELECT * FROM linies";
		return dbu.execSelect(query);
	}
	
		
	public List<TreeMap<Integer, List<String>>> getRouts(Integer station_start, Integer station_end) throws ClassNotFoundException, SQLException{
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
		
		makeIterationLinie(routes,linies);
		makeIterationChangeLinie(routes,linies);
		//makeIterationLinie(routes,linies);
		//makeIterationLinie(routes,linies);
		return routes;
	}
	
	List<TreeMap<Integer, List<String>>> makeIterationLinie(List<TreeMap<Integer, List<String>>> routes, ArrayList<ArrayList<String>> linies) {
		//choose last elements in HashMaps
		boolean[] isStationUpChanged = new boolean[routes.size()];
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<TreeMap<Integer, List<String>>> tempRoutes = (List<TreeMap<Integer, List<String>>>) jhu.deepClone(routes);
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
	
	
	
	List<TreeMap<Integer, List<String>>> makeIterationChangeLinie(List<TreeMap<Integer, List<String>>> routes, ArrayList<ArrayList<String>> linies) {
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<TreeMap<Integer, List<String>>> tempRoutes = new ArrayList<TreeMap<Integer, List<String>>>();
		//String[] arrayStationDown = new String[routes.size()];
		//int hmCounter = 0;
		for (TreeMap<Integer, List<String>> hm : routes) {
			Entry<Integer, List<String>> lastEntry = hm.lastEntry();
			//choose last elements in List
			Integer listSize = lastEntry.getValue().size();
			System.out.println(lastEntry.getKey());
			System.out.println(lastEntry.getValue().get(listSize - 1));
			Integer lastKey = lastEntry.getKey();
			String lastListValue = lastEntry.getValue().get(listSize - 1);
			TreeMap<Integer, List<String>> hmNewLinies = getChangeLinieStations(linies, lastKey, lastListValue);
			System.out.println("hmNewLinies: " + hmNewLinies);
			//TreeMap<Integer, List<String>> hmTemp = (TreeMap<Integer, List<String>>) jhu.deepClone(hm);
			//hm.putAll(hmNewLinies);
			for (Map.Entry<Integer, List<String>> entry : hmNewLinies.entrySet()) {
				Integer key = entry.getKey();
			    List<String> value = entry.getValue();
			    System.out.println("hm.put(key, value): " + hm.put(key, value));
			    tempRoutes.add((TreeMap<Integer, List<String>>) hm.put(key, value));
			    System.out.println("tempRoutes in cicle: " + tempRoutes);
			}	
			
		}
		return tempRoutes;
	}
	
	TreeMap<Integer, List<String>> getChangeLinieStations(ArrayList<ArrayList<String>> linies, Integer lastKey, String lastListValue){
		final Integer LINIES_DESC_ID = 1;
		final Integer STATION_ID = 2;
		TreeMap<Integer, List<String>> hmNewLinies = new TreeMap<Integer, List<String>>();
		for(ArrayList<String> row : linies){
			if (row.get(STATION_ID).equals(lastListValue) && !row.get(LINIES_DESC_ID).equals(lastKey.toString())){
				hmNewLinies.put(Integer.parseInt(row.get(LINIES_DESC_ID)), new ArrayList(Arrays.asList(row.get(STATION_ID))));
			}
		}
		return hmNewLinies;
	}
		
}
