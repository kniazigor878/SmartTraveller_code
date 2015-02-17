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
import java.util.LinkedHashMap;
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
	
		
	public List<LinkedHashMap<Integer, List<String>>> getRouts(Integer station_start, Integer station_end) throws ClassNotFoundException, SQLException{
		//List<List<HashMap<Integer, List<String>>>> routes = new ArrayList<List<HashMap<Integer,List<String>>>>();
		List<LinkedHashMap<Integer, List<String>>> routes = new ArrayList<LinkedHashMap<Integer,List<String>>>();
		LinkedHashMap<Integer, List<String>> route = new LinkedHashMap<Integer,List<String>>();
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
				routes.add((LinkedHashMap<Integer, List<String>>) route.clone());
				//routes.add(this.cloneListHm(route));
				tempList_origin.clear();
				route.clear();
				//routes.clear();
			}
			//System.out.println(row.get(2));
		}
		
		makeIterationLinie(routes,linies);
		routes = makeIterationChangeLinie(routes,linies);
		makeIterationLinie(routes,linies);
		routes = makeIterationChangeLinie(routes,linies);
		return routes;
	}
	
	List<LinkedHashMap<Integer, List<String>>> makeIterationLinie(List<LinkedHashMap<Integer, List<String>>> routes, ArrayList<ArrayList<String>> linies) {
		//choose last elements in HashMaps
		boolean[] isStationUpChanged = new boolean[routes.size()];
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<LinkedHashMap<Integer, List<String>>> tempRoutes = (List<LinkedHashMap<Integer, List<String>>>) jhu.deepClone(routes);
		String[] arrayStationDown = new String[routes.size()];
		int hmCounter = 0;
		for (LinkedHashMap<Integer, List<String>> hm : routes) {
			Entry<Integer, List<String>> lastEntry =  jhu.getHmLastEntry(hm);
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
			LinkedHashMap<Integer, List<String>> hmWithNewLinieStations = null;
			if(stationUp != ""){
				hmWithNewLinieStations = addNewLinieStations(hm,stationUp);
				System.out.println("routes: " + routes);
			}
			arrayStationDown[hmCounter] = stationDown;
			hmCounter++;
			//make iteration:
			//1) in one direction in linie
			//2) in other direction in linie
			//3) change linie
			//4) change station in current local
		}
		
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
	
	LinkedHashMap<Integer, List<String>> addNewLinieStations(LinkedHashMap<Integer, List<String>> hm, String stationUp){
		boolean isStationAlreadyExist = false;
		JavaHelpUtils jhu = new JavaHelpUtils();
		for(List<String> valueList: hm.values()){
			for(String value: valueList){
				if(value.equals(stationUp)){
					isStationAlreadyExist = true;
				}
			}
		}
		
		if(!isStationAlreadyExist){
			Entry<Integer, List<String>> lastEntry = jhu.getHmLastEntry(hm);
			lastEntry.getValue().add(stationUp);
		}
		return hm;
	}
	
	
	
	List<LinkedHashMap<Integer, List<String>>> makeIterationChangeLinie(List<LinkedHashMap<Integer, List<String>>> routes, ArrayList<ArrayList<String>> linies) {
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<LinkedHashMap<Integer, List<String>>> tempRoutes = (List<LinkedHashMap<Integer, List<String>>>) jhu.deepClone(routes);
		for (LinkedHashMap<Integer, List<String>> hm : routes) {
			Entry<Integer, List<String>> lastEntry = jhu.getHmLastEntry(hm);
			//choose last elements in List
			Integer listSize = lastEntry.getValue().size();
			Integer lastKey = lastEntry.getKey();
			String lastListValue = lastEntry.getValue().get(listSize - 1);
			LinkedHashMap<Integer, List<String>> hmNewLinies = getChangeLinieStations(linies, lastKey, lastListValue);
			for (Map.Entry<Integer, List<String>> entry : hmNewLinies.entrySet()) {
				Integer key = entry.getKey();
			    List<String> value = entry.getValue();
				LinkedHashMap<Integer, List<String>> hmTemp = (LinkedHashMap<Integer, List<String>>) jhu.deepClone(hm);
				hmTemp.put(key, value);
			    tempRoutes.add(hmTemp);
			}	
			
		}
		routes = (List<LinkedHashMap<Integer, List<String>>>) jhu.deepClone(tempRoutes);
		System.out.println("routes: " + routes);
		return routes;
	}
	
	LinkedHashMap<Integer, List<String>> getChangeLinieStations(ArrayList<ArrayList<String>> linies, Integer lastKey, String lastListValue){
		final Integer LINIES_DESC_ID = 1;
		final Integer STATION_ID = 2;
		LinkedHashMap<Integer, List<String>> hmNewLinies = new LinkedHashMap<Integer, List<String>>();
		for(ArrayList<String> row : linies){
			if (row.get(STATION_ID).equals(lastListValue) && !row.get(LINIES_DESC_ID).equals(lastKey.toString())){
				hmNewLinies.put(Integer.parseInt(row.get(LINIES_DESC_ID)), new ArrayList(Arrays.asList(row.get(STATION_ID))));
			}
		}
		return hmNewLinies;
	}
		
}
