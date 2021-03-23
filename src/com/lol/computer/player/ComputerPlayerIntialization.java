package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.constant.Constants;

/*
 * This class is used to initialize the computer player
 */
public class ComputerPlayerIntialization {
	private static Integer count = 1;

	/**
	 * This method is used to initialize the data structure in the form of
	 * circular linked list
	 * @param numOfPlayers
	 *            -Number of players playing the game
	 */
	public static void initTerrianTokenMap(Integer numOfPlayers) {
		Map<String, Map<String, Integer>> terrianMap = new HashMap<>();
		List<String> location = new ArrayList<String>();
		location.add(Constants.NORTH_CHAR);
		location.add(Constants.NORTH_EAST_CHAR);
		location.add(Constants.NORTH_WEST_CHAR);
		location.add(Constants.SOUTH_CHAR);
		location.add(Constants.SOUTH_EAST_CHAR);
		location.add(Constants.SOUTH_WEST_CHAR);
		location.add(Constants.WEST_CHAR);
		location.add(Constants.EAST_CHAR);

		location.stream().forEach(loc -> {
			Map<String, Map<String, Integer>> terrainList = new HashMap<>();
			terrainList.put(count + Constants.MOUNTAINS_CHAR, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), numOfPlayers));
			terrainList.put(count + Constants.BEACH_CHAR, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), numOfPlayers));
			terrainList.put(count + Constants.FOREST_CHAR, ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), numOfPlayers));
			terrianMap.putAll(terrainList);
			ComputerPlayer.getInstance().add(terrainList, loc, count);
			count++;
		});
		ComputerPlayer.getInstance().setTreasureLoc(new HashSet<>());
		ComputerPlayer.getInstance().setAllPlayerTrrianMap(terrianMap);
	}

	/**
	 * This method is used the update the personal, open and bonus information
	 * at the start of game
	 * @param messageDetailsList
	 * @param playerName
	 * @param PlayerList
	 */
	public static void updatePersonalTokenMap(List<String> messageDetailsList, String playerName,
			List<String> PlayerList) {
		for (String player : PlayerList) {
			// marking 1 for having the token
			for (String terrianToken : messageDetailsList) {
				Map<String, Integer> terrianMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap()
						.get(terrianToken);
				if (terrianMap != null) {
					if (!playerName.equals(player))
						terrianMap.put(player, 0);
					else
						terrianMap.put(player, 1);
					ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrianToken, terrianMap);
				}
			}
		}
	}

	/**
	 * This method is used to update the location with playing player, which is not with him
	 * @param playerName
	 */
	public static void setAllLocationAsZeroToPlayer(String playerName) {
		Set<String> messageDetailsList = ComputerPlayer.getInstance().getAllPlayerTrrianMap().keySet();
		for (String terrianToken : messageDetailsList) {
			Map<String, Integer> terrianMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrianToken);
			terrianMap.put(playerName, 0);
		}

	}

	/**
	 * This is helper method is used to map direction with integer number which make easy to traverse
	 */
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
