package by.iharkaratkou.bsnlogic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import by.iharkaratkou.*;
import by.iharkaratkou.db.DatabaseUtils;
import by.iharkaratkou.javautils.JavaHelpUtils;


public class BusinessLogicUtils {
	public ArrayList<ArrayList<String>> getStations() throws ClassNotFoundException, SQLException{
		DatabaseUtils dbu = new DatabaseUtils();
		String query = "SELECT * FROM stations";
		return dbu.execSelect(query);
	}
	
	public ArrayList<ArrayList<String>> getLiniesDesc() throws ClassNotFoundException, SQLException{
		DatabaseUtils dbu = new DatabaseUtils();
		String query = "SELECT * FROM linies_desc";
		return dbu.execSelect(query);
	}
	public String getLiniesNameById(String linieId){
		ArrayList<ArrayList<String>> linies_desc = new ArrayList<ArrayList<String>>();
		final Integer LINIES_DESC_ID = 0;
		final Integer LINIES_DESC_NAME = 2;
		String linieName = "";
		try {
			linies_desc = getLiniesDesc();
			for(ArrayList<String> row : linies_desc){
				if (linieId.equals(row.get(LINIES_DESC_ID))){
					linieName = row.get(LINIES_DESC_NAME);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
		return linieName;
	}
	
	public String getStationNameById(String stationId){
		ArrayList<ArrayList<String>> stations = new ArrayList<ArrayList<String>>();
		final Integer LINIES_STATION_ID = 0;
		final Integer LINIES_STATION_NAME = 2;
		String stationName = "";
		try {
			stations = getStations();
			for(ArrayList<String> row : stations){
				if (stationId.equals(row.get(LINIES_STATION_ID))){
					stationName = row.get(LINIES_STATION_NAME);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
		return stationName;
	}

	public String getLocalityByStationId(String stationId){
		String localityId = "";
		ArrayList<ArrayList<String>> localityStation = new ArrayList<ArrayList<String>>();
		try {
			localityStation = getLocalityStation();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		final Integer LOCALITY_ID = 1;
		final Integer STATION_ID = 2;
		for(ArrayList<String> row : localityStation){
			if (stationId.equals(row.get(STATION_ID))){
				localityId = row.get(LOCALITY_ID);
			}
		}
		return localityId;
	}
	
	public ArrayList<ArrayList<String>> getRoutesForPrint (List<LinkedHashMap<Integer, List<String>>> routes){
		JavaHelpUtils jhu = new JavaHelpUtils();
		ArrayList<ArrayList<String>> routesForPrint = new ArrayList<ArrayList<String>>();
		for (LinkedHashMap<Integer, List<String>> hm : routes){
			LinkedHashMap<String, List<String>> hmTemp = new LinkedHashMap<String, List<String>>();
			System.out.println(hm.keySet());
			List<String> routeLocalities = new ArrayList<String>();
			for(Integer key: hm.keySet()){
				//String keyName = getLiniesNameById(key.toString());
				for(String stationId: hm.get(key)){
					routeLocalities.add(getLocalityByStationId(stationId));
				}
				//hmTemp.put((String) jhu.deepClone(keyName), (List<String>) jhu.deepClone(stationNames));
			}
			routesForPrint.add( (ArrayList<String>) jhu.deepClone(routeLocalities));
		}
		
		return routesForPrint;
	}	
	
	public List<LinkedHashMap<String, List<String>>> getRoutesForView (List<LinkedHashMap<Integer, List<String>>> routes){
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<LinkedHashMap<String, List<String>>> routesForView = new ArrayList<LinkedHashMap<String, List<String>>>();
		for (LinkedHashMap<Integer, List<String>> hm : routes){
			LinkedHashMap<String, List<String>> hmTemp = new LinkedHashMap<String, List<String>>();
			for(Integer key: hm.keySet()){
				String keyName = getLiniesNameById(key.toString());
				List<String> stationNames = new ArrayList<String>();
				for(String stationId: hm.get(key)){
					stationNames.add(getStationNameById(stationId));
				}
				hmTemp.put((String) jhu.deepClone(keyName), (List<String>) jhu.deepClone(stationNames));
			}
			routesForView.add((LinkedHashMap<String, List<String>>) jhu.deepClone(hmTemp));
		}
		
		return routesForView;
	}
	
	public ArrayList<ArrayList<String>> getLinies() throws ClassNotFoundException, SQLException{
		DatabaseUtils dbu = new DatabaseUtils();
		String query = "SELECT * FROM linies";
		return dbu.execSelect(query);
	}
	
	public ArrayList<ArrayList<String>> getLocalityStation() throws ClassNotFoundException, SQLException{
		DatabaseUtils dbu = new DatabaseUtils();
		String query = "SELECT * FROM locality_station";
		return dbu.execSelect(query);
	}
	
	public ArrayList<ArrayList<String>> getLocalities() throws ClassNotFoundException, SQLException{
		DatabaseUtils dbu = new DatabaseUtils();
		String query = "SELECT * FROM localities";
		return dbu.execSelect(query);
	}
	
	public List<LinkedHashMap<Integer, List<String>>> getRoutsByLocalities(Integer locality_start, Integer locality_end) throws ClassNotFoundException, SQLException{
		ArrayList<ArrayList<String>> localityStation = getLocalityStation();
		final Integer LOCALITY_ID = 1;
		final Integer STATION_ID = 2;
		ArrayList<String> stationsStart = new ArrayList<String>();
		ArrayList<String> stationsEnd = new ArrayList<String>();
		for(ArrayList<String> row : localityStation){
			if (locality_start.toString().equals(row.get(LOCALITY_ID))){
				stationsStart.add(row.get(STATION_ID));
			}
			if (locality_end.toString().equals(row.get(LOCALITY_ID))){
				stationsEnd.add(row.get(STATION_ID));
			}
		}
		//System.out.println(stationsStart);
		//System.out.println(stationsEnd);
		List<LinkedHashMap<Integer, List<String>>> finalRoutesByLocalities = new ArrayList<LinkedHashMap<Integer, List<String>>>();
		for(String station_start: stationsStart){
			for(String station_end: stationsEnd){
				System.out.println(station_start + " " + station_end);
				finalRoutesByLocalities.addAll(getRoutsByStations(Integer.parseInt(station_start),Integer.parseInt(station_end),localityStation));
			}
		}
		
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<LinkedHashMap<Integer, List<String>>> finalRoutesByLocalitiesSmall = new ArrayList<LinkedHashMap<Integer, List<String>>>();
		final int finalRoutesByLocalitiesSmallLimit = 7;
		outerloop:
		for(int i = 0; i < 10; i++){
			for (LinkedHashMap<Integer, List<String>> hm : finalRoutesByLocalities) {
				if(hm.size() == i){
					finalRoutesByLocalitiesSmall.add((LinkedHashMap<Integer, List<String>>) jhu.deepClone(hm));
					if(finalRoutesByLocalitiesSmall.size() == finalRoutesByLocalitiesSmallLimit) break outerloop;
				}
			}
			//if(finalRoutesByLocalitiesSmall.size() == finalRoutesByLocalitiesSmallLimit) break;
		}
		
		return finalRoutesByLocalitiesSmall;
	}
	
	public List<LinkedHashMap<Integer, List<String>>> getRoutsByStations(Integer station_start, Integer station_end,ArrayList<ArrayList<String>> localityStation) throws ClassNotFoundException, SQLException{
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
				tempList_origin.clear();
				route.clear();
			}
		}
		//System.out.println("routes: " + routes);
		List<LinkedHashMap<Integer, List<String>>> finalRoutes = new ArrayList<LinkedHashMap<Integer,List<String>>>();
		for(int i = 0; i<2; i++){
			System.out.println(i);
			System.out.println("before makeIterationLinie");
			makeIterationLinie(routes,linies);
			finalRoutes = checkDestination(routes,station_end,finalRoutes);
			//System.out.println("finalRoutes1: " + finalRoutes);
			deleteRoutesDuplicates(finalRoutes);
			deleteRoutesDuplicates(routes);
			routes.removeAll(finalRoutes);
			System.out.println("before makeIterationChangeLinie");
			routes = makeIterationChangeLinie(routes,linies);
			//System.out.println("routes: " + routes);
			deleteRoutesDuplicates(routes);
			finalRoutes = checkDestination(routes,station_end,finalRoutes);
			deleteRoutesDuplicates(finalRoutes);
			System.out.println("before makeIterationChangeStationInLocality");
			routes = makeIterationChangeStationInLocality(routes,localityStation,linies);
			deleteRoutesDuplicates(routes);
			finalRoutes = checkDestination(routes,station_end,finalRoutes);
			deleteRoutesDuplicates(finalRoutes);			
			routes.removeAll(finalRoutes);
		}
		//System.out.println("finalRoutes: " + finalRoutes);
		return finalRoutes;
	}
	
	List<LinkedHashMap<Integer, List<String>>> makeIterationChangeStationInLocality(List<LinkedHashMap<Integer, List<String>>> routes,ArrayList<ArrayList<String>> localityStation, ArrayList<ArrayList<String>> linies){
		JavaHelpUtils jhu = new JavaHelpUtils();
		List<LinkedHashMap<Integer, List<String>>> tempRoutes = (List<LinkedHashMap<Integer, List<String>>>) jhu.deepClone(routes);
		for (LinkedHashMap<Integer, List<String>> hm : routes) {
			//System.out.println("hm before: " + hm);
			LinkedHashMap<Integer, List<String>> hmTemp = (LinkedHashMap<Integer, List<String>>) jhu.deepClone(hm);
			Entry<Integer, List<String>> lastEntry =  jhu.getHmLastEntry(hm);
			//choose last elements in List
			Integer listSize = lastEntry.getValue().size();
			Integer lastKey = lastEntry.getKey();
			String lastListValue = lastEntry.getValue().get(listSize - 1);
			ArrayList<String> otherStationsInLocality = getOtherStationsInLocality(localityStation,lastListValue);
			LinkedHashMap<Integer,String> hmOtherStations = new LinkedHashMap<Integer,String>();
			if (otherStationsInLocality.size() != 0){
				for(String otherStation: otherStationsInLocality){
					hmOtherStations.putAll(getHmOtherStations(linies,otherStation,lastKey));
				}
			}
			//System.out.println("hmOtherStations: " + hmOtherStations);
			Entry<Integer, List<String>> entryHmOtherStations = null;
			Iterator i = hmOtherStations.entrySet().iterator();
			while(i.hasNext()){
				entryHmOtherStations = (Entry<Integer, List<String>>) i.next();
				Integer keyHmOtherStations = entryHmOtherStations.getKey();
				List<String> valueHmOtherStations = new ArrayList<String>();
				valueHmOtherStations.add(hmOtherStations.get(keyHmOtherStations));
				
				if(!hm.containsKey(keyHmOtherStations)){
					hm.put(keyHmOtherStations, valueHmOtherStations);
					//System.out.println("hm after: " + hm);
					tempRoutes.add((LinkedHashMap<Integer, List<String>>) jhu.deepClone(hm));
					hm.clear();
					hm = (LinkedHashMap<Integer, List<String>>) jhu.deepClone(hmTemp);
				}
			}
		}	
		
		routes = (List<LinkedHashMap<Integer, List<String>>>) jhu.deepClone(tempRoutes);
		
		return routes;
	}
	
	HashMap<Integer,String> getHmOtherStations(ArrayList<ArrayList<String>> linies, String otherStation, Integer lastKey){
		HashMap<Integer,String> hmOtherStations = new HashMap<Integer,String>();
		final Integer LINIES_DESC_ID = 1;
		final Integer STATION_ID = 2;
		for(ArrayList<String> row : linies){
			if (otherStation.equals(row.get(STATION_ID)) && !lastKey.equals(Integer.parseInt(row.get(LINIES_DESC_ID)))){
				hmOtherStations.put(Integer.parseInt(row.get(LINIES_DESC_ID)), row.get(STATION_ID));
			}
		}
		return hmOtherStations;
	}
	
	ArrayList<String> getOtherStationsInLocality(ArrayList<ArrayList<String>> localityStation,String lastListValue){
		ArrayList<String> otherStationsInLocalityLoc = new ArrayList<String>();
		final Integer LOCALITY_ID = 1;
		final Integer STATION_ID = 2;
		String locality_id = null;
		for(ArrayList<String> row : localityStation){
			if (lastListValue.equals(row.get(STATION_ID))){
				locality_id = row.get(LOCALITY_ID);
			}
		}
		for(ArrayList<String> row : localityStation){
			if (!lastListValue.equals(row.get(STATION_ID)) && locality_id.equals(row.get(LOCALITY_ID))){
				otherStationsInLocalityLoc.add(row.get(STATION_ID));
			}
		}
		
		return otherStationsInLocalityLoc;
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
			Integer lastKey = lastEntry.getKey();
			String lastListValue = lastEntry.getValue().get(listSize - 1);
			String stationUp = getStation(linies,lastKey,lastListValue,"Up");
			//System.out.println("stationUp: " + stationUp);
			String stationDown = getStation(linies,lastKey,lastListValue,"Down");
			//System.out.println("stationDown: " + stationDown);
			
			LinkedHashMap<Integer, List<String>> hmWithNewLinieStations = null;
			if(stationUp != ""){
				hmWithNewLinieStations = addNewLinieStations(hm,stationUp);
				//System.out.println("hmWithNewLinieStations: " + hmWithNewLinieStations);
				//System.out.println("hm: " + hm);
				//System.out.println("routes: " + routes);
			}
			arrayStationDown[hmCounter] = stationDown;
			hmCounter++;
			//make iteration:
			//1) in one direction in linie
			//2) in other direction in linie
			//3) change linie
			//4) change station in current local
		}
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
		//System.out.println("addNewLinieStations starts");
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
			//System.out.println("hm in addNewLinieStations: " + hm + " lastEntry in addNewLinieStations: " + lastEntry + " stationUp in addNewLinieStations: " + stationUp + " lastEntry.getValue(): " + lastEntry.getValue());
			lastEntry.getValue().add(stationUp);
			//System.out.println("lastEntry in addNewLinieStations after: " + lastEntry);
		}
		//System.out.println("addNewLinieStations ends");
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
				if(!hmTemp.containsKey(key)){
					hmTemp.put(key, value);
				}
			    tempRoutes.add(hmTemp);
			}	
			
		}
		routes = (List<LinkedHashMap<Integer, List<String>>>) jhu.deepClone(tempRoutes);
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
	
	List<LinkedHashMap<Integer, List<String>>> deleteRoutesDuplicates(List<LinkedHashMap<Integer, List<String>>> routes){
		
		HashSet<LinkedHashMap<Integer, List<String>>> hs = new HashSet<LinkedHashMap<Integer, List<String>>>();
		hs.addAll(routes);
		routes.clear();
		routes.addAll(hs);
		return routes;
	}
	
	List<LinkedHashMap<Integer, List<String>>> checkDestination(List<LinkedHashMap<Integer, List<String>>> routes, Integer station_end, List<LinkedHashMap<Integer, List<String>>> finalRoutes){
		JavaHelpUtils jhu = new JavaHelpUtils();
		for(LinkedHashMap<Integer, List<String>> hm: routes){
			//System.out.println("hm in checkDestination: " + hm);
			Entry<Integer, List<String>> lastEntry = jhu.getHmLastEntry(hm);
			Integer listSize = lastEntry.getValue().size();
			String lastListValue = lastEntry.getValue().get(listSize - 1);
			if(station_end.equals(Integer.parseInt(lastListValue))){
				finalRoutes.add(hm);
			}
		}
		return finalRoutes;
	}
		
}
