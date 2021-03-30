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
	 * 
	 * @param numOfPlayers
	 *            -Number of players playing the game
	 */
	public static void initTerrainTokenMap(Integer numOfPlayers) {
		Map<String, Map<String, Integer>> terrainMap = new HashMap<>();
		List<String> location = new ArrayList<String>();
		location.add(Constants.NORTH_CHAR);
		location.add(Constants.NORTH_EAST_CHAR);
		location.add(Constants.EAST_CHAR);
		location.add(Constants.SOUTH_EAST_CHAR);
		location.add(Constants.SOUTH_CHAR);
		location.add(Constants.SOUTH_WEST_CHAR);
		location.add(Constants.WEST_CHAR);
		location.add(Constants.NORTH_WEST_CHAR);

		location.stream().forEach(loc -> {
			Map<String, Map<String, Integer>> terrainList = new HashMap<>();
			terrainList.put(count + Constants.MOUNTAINS_CHAR,
					ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), numOfPlayers));
			terrainList.put(count + Constants.BEACH_CHAR,
					ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), numOfPlayers));
			terrainList.put(count + Constants.FOREST_CHAR,
					ComputerPlayer.getInstance().setPlayerObj(new HashMap<>(), numOfPlayers));
			terrainMap.putAll(terrainList);
			ComputerPlayer.getInstance().add(terrainList, loc, count);
			count++;
		});
		ComputerPlayer.getInstance().setTreasureLoc(new HashSet<>());
		ComputerPlayer.getInstance().setAllPlayerTrrianMap(terrainMap);
	}

	/**
	 * This method is used the update the personal, open and bonus information
	 * at the start of game
	 * 
	 * @param messageDetailsList
	 * @param playerName
	 * @param PlayerList
	 */
	public static void updatePersonalTokenMap(List<String> messageDetailsList, String playerName,
			List<String> PlayerList) {
		for (String player : PlayerList) {
			// marking 1 for having the token
			for (String terrainToken : messageDetailsList) {
				Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap()
						.get(terrainToken);
				if (terrainMap != null) {
					if (!playerName.equals(player))
						terrainMap.put(player, 0);
					else
						terrainMap.put(player, 1);
					ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken, terrainMap);
				}
			}
		}
	}

	/**
	 * This method is used to update the location with playing player, which is
	 * not with him
	 * 
	 * @param playerName
	 */
	public static void setAllLocationAsZeroToPlayer(String playerName) {
		Set<String> messageDetailsList = ComputerPlayer.getInstance().getAllPlayerTrrianMap().keySet();
		for (String terrainToken : messageDetailsList) {
			Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrainToken);
			terrainMap.put(playerName, 0);
		}

	}
}
