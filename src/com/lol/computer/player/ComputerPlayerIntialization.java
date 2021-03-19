package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class ComputerPlayerIntialization {
	static int i = 0;

	public static void initTerrianTokenMap(Integer playerNumber) {
		int noPlayer = PlayerInformation.getInstance().getNumberOfPlayers();
		Map<String, Integer> playerList = new HashMap<>();
		for (int no = 1; no <= noPlayer; no++) {
			String playerName = "P" + String.valueOf(no);
			playerList.put(playerName, -1);
		}
		List<String> location = new ArrayList<String>();
		location.add(Constants.NORTH_CHAR);
		location.add(Constants.NORTH_EAST_CHAR);
		location.add(Constants.NORTH_WEST_CHAR);
		location.add(Constants.SOUTH_CHAR);
		location.add(Constants.SOUTH_EAST_CHAR);
		location.add(Constants.SOUTH_WEST_CHAR);
		location.add(Constants.WEST_CHAR);
		location.add(Constants.EAST_CHAR);
		Map<String, Map<String, Integer>> terrainList = new HashMap<>();

		location.stream().forEach(loc -> {
			i++;
			terrainList.put(i + Constants.MOUNTAINS_CHAR, playerList);
			terrainList.put(i + Constants.BEACH_CHAR, playerList);
			terrainList.put(i + Constants.FOREST_CHAR, playerList);

		});
		ComputerPlayer.getInstance().setAllPlayerTrrianMap(terrainList);
		System.out.println("terrainList " + terrainList);
	}

	public static void updatePersonalTokenMap(List<String> messageDetailsList, String playerName,
			List<String> PlayerList) {
		  	
		messageDetailsList.stream().forEach(token -> {
			
			ComputerPlayer.getInstance().getAllPlayerTrrianMap().entrySet().stream().filter(terrianToken -> terrianToken.getKey().equals(token)).forEach(
					terrianMap -> {
						HashMap<String, Integer> map = new HashMap<>();

							terrianMap.getValue().entrySet().stream().forEach(playerMap -> {
										if(playerMap.getKey().equals(playerName)) {
											map.put(playerMap.getKey(), 1);
										}
										else
											map.put(playerMap.getKey(), 0);
										
								});
							terrianMap.setValue(map);
							}
					);
		});
		System.out.println("terrian map" + ComputerPlayer.getInstance().getAllPlayerTrrianMap());
	}

	public static void updateDirectionIntegerMap() {
		Map<String, Integer> dirIntMap = new HashMap<>();
		dirIntMap.put(Constants.NORTH_CHAR, 1);
		dirIntMap.put(Constants.NORTH_EAST_CHAR, 2);
		dirIntMap.put(Constants.EAST_CHAR, 3);
		dirIntMap.put(Constants.SOUTH_EAST_CHAR, 4);
		dirIntMap.put(Constants.SOUTH_CHAR, 5);
		dirIntMap.put(Constants.SOUTH_WEST_CHAR, 6);
		dirIntMap.put(Constants.WEST_CHAR, 7);
		dirIntMap.put(Constants.NORTH_WEST_CHAR, 8);
		ComputerPlayer.getInstance().setDirIntMap(dirIntMap);
		Map<Integer, Set<String>> directionIntegerMap = new HashMap<>();

		for (int i = 1; i <= 8; i++) {
			Set<String> terrianToken = new HashSet<>();
			terrianToken.add(i + "M");
			terrianToken.add(i + "B");
			terrianToken.add(i + "F");
			directionIntegerMap.put(i, terrianToken);
		}
		ComputerPlayer.getInstance().setDirectionIntegerMap(directionIntegerMap);

	}
}
