package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class ComputerPlayerIntialization {

	public static void main(String[] args) {

		initTerrianTokenMap(3);

		List<String> messageDetailsList = new ArrayList<>();
		messageDetailsList.add("1M");
		messageDetailsList.add("2M");
		messageDetailsList.add("2B");
		messageDetailsList.add("3F");
		messageDetailsList.add("8B");
		String playerName = "P1";
		List<String> PlayerList = new ArrayList<>();
		PlayerList.add("P1");
		PlayerList.add("P2");
		PlayerList.add("P3");

		updatePersonalTokenMap(messageDetailsList, playerName, PlayerList);
		// checkIsTreasureLocFound() ;

	}

	public static void initTerrianTokenMap(Integer playerNumber) {
		Map<String, Map<String, Integer>> terrianMap = new HashMap<>();

		terrianMap.put(Constants.MOUNTAIN_1, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_2, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_3, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_4, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_5, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_6, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_7, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.MOUNTAIN_8, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));

		terrianMap.put(Constants.FOREST_1, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_2, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_3, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_4, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_5, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_6, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_7, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.FOREST_8, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));

		terrianMap.put(Constants.BEACH_1, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_2, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_3, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_4, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_5, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_6, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_7, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));
		terrianMap.put(Constants.BEACH_8, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), playerNumber));

		Set<String> treasureLoc = new HashSet<>();

		ComputerPlayer.getInstance().setTreasureLoc(treasureLoc);
		ComputerPlayer.getInstance().setAllPlayerTrrianMap(terrianMap);
		System.out.println("terrian map" + terrianMap);
	}

	public static void updatePersonalTokenMap(List<String> messageDetailsList, String playerName,
			List<String> PlayerList) {

		for (String player : PlayerList) {
			// marking 1 for having the token
			for (String terrianToken : messageDetailsList) {
				Map<String, Integer> terrianMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap()
						.get(terrianToken);
				if (!playerName.equals(player)) {
					terrianMap.put(player, 0);
				} else {
					terrianMap.put(player, 1);
				}
				ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(player, terrianMap);
			}
		}
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
