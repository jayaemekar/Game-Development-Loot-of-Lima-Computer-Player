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

public class ComputerPlayerDeductionLogic {

	/**
	 * This method used to process the answer message for deduction
	 * 
	 * @param messageDetailsList
	 */
	public static void processAnswerMessage(List<String> messageDetailsList) {
		String playerName = messageDetailsList.get(4);
		String Diretion1 = messageDetailsList.get(0).substring(0, 2);
		String Diretion2 = messageDetailsList.get(1).substring(0, 2);

		Set<String> terrainToken = getTokenBetweenDirectionUsingList(ComputerPlayer.getInstance().createNode(Diretion1),
				ComputerPlayer.getInstance().createNode(Diretion2));
		// remove already identified non treasure location from terrain token
		// Map present between the two selected direction as deduced map
		Set<String> deducedterrainToken = updateDeducedPlayerTokenMap(terrainToken).keySet();
		// Get intersection of terrainToken and deducedterrainToken
		System.out.println("terrain needs to be processed for deduction : " + terrainToken);
		// deduce terrain locations map based on answer given by player
		if (terrainToken != null && !terrainToken.isEmpty()) {
			updateDeducedterrainMap(deducedterrainToken, terrainToken, playerName,
					PlayerInformation.getInstance().getPlayerNameList(), messageDetailsList);

		}
		// after answer processing check is treasure location found or not
		checkIsTreasureLocFound();
	}

	/**
	 * This method is used to find out the the terrain location between two
	 * direction
	 * 
	 * @param direction1
	 * @param direction2
	 * @return
	 */
	private static Set<String> getTokenBetweenDirectionUsingList(Node direction1, Node direction2) {
		Set<String> terrainToken = new HashSet<>();
		if (direction1.direction.equals(direction2.direction)) {
			return ComputerPlayer.getInstance().getAllPlayerTrrianMap().keySet();
		} else {
			while (direction1 != direction2) {
				direction1.terrainList.entrySet().stream().forEach(terrain -> {
					terrainToken.add(terrain.getKey());
				});
				direction1 = direction1.next;
			}
		}
		return terrainToken;
	}

	/**
	 * This method is used to update the map with the deduced terrains
	 * 
	 * @param deducedterrainToken
	 * @param terrainToken
	 * @param playerName
	 * @param playerList
	 * @param messageDetailsList
	 */
	public static void updateDeducedterrainMap(Set<String> deducedterrainToken, Set<String> terrainToken,
			String playerName, List<String> playerList, List<String> messageDetailsList) {
		String noIfTokens = messageDetailsList.get(3);
		String areaToken = messageDetailsList.get(2);

		if (Integer.valueOf(noIfTokens) == 0) {
			if (Constants.BEACH_CHAR.equals(areaToken)) {
				Set<String> deducedBeachLoc = ComputerPlayer.getInstance().getDeducedBeachLoc();
				updateZeroterrainTokenInformation(deducedBeachLoc, playerName, terrainToken);
			} else if (Constants.FOREST_CHAR.equals(areaToken)) {
				Set<String> deducedForestLoc = ComputerPlayer.getInstance().getDeducedForestLoc();
				updateZeroterrainTokenInformation(deducedForestLoc, playerName, terrainToken);
			} else if (Constants.MOUNTAINS_CHAR.equals(areaToken)) {
				Set<String> deducedMountainLoc = ComputerPlayer.getInstance().getDeducedMountainLoc();
				updateZeroterrainTokenInformation(deducedMountainLoc, playerName, terrainToken);
			} else {
				Set<String> deducedAllTokenLoc = ComputerPlayer.getInstance().getDeducedPlayerTokenMap().keySet();
				updateZeroterrainTokenInformation(deducedAllTokenLoc, playerName, terrainToken);
			}
		} else if (Integer.valueOf(noIfTokens) == terrainToken.size()) {
			ComputerPlayerIntialization.updatePersonalTokenMap(terrainToken.stream().collect(Collectors.toList()),
					playerName, PlayerInformation.getInstance().getPlayerNameList());
		}

	}

	/**
	 * Mark remaining tokens of playing player as zero.
	 * 
	 */
	private static void updateZeroterrainTokenInformation(Set<String> deducedterrainLoc, String playerName,
			Set<String> terrainToken) {
		List<String> updatedterrainList = new ArrayList<>();
		for (String terrain : terrainToken) {
			if (deducedterrainLoc.contains(terrain)) {
				updatedterrainList.add(terrain);
				updateterrainMapForNotTreasureLoc(updatedterrainList, playerName);
			}
		}
	}

	/**
	 * create and update deduced player token map depending upon given direction
	 * 
	 * @param terrainDirectionMap
	 * @return
	 */
	public static Map<String, Map<String, Integer>> updateDeducedPlayerTokenMap(Set<String> terrainDirectionMap) {

		Map<String, Map<String, Integer>> deducedPlayerTokenMap = new HashMap<>();
		for (String terrain : terrainDirectionMap) {
			Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain);
			int sum = terrainMap.values().stream().reduce(0, Integer::sum);
			if (sum == 0) {
				Set<String> treasureLocSet = ComputerPlayer.getInstance().getTreasureLoc();
				treasureLocSet.add(terrain);
				ComputerPlayer.getInstance().setTreasureLoc(treasureLocSet);
			} else if (sum == -3 || sum == -2 || sum == -1) {
				deducedPlayerTokenMap.put(terrain, terrainMap);
			}

		}
		ComputerPlayer.getInstance().setDeducedPlayerTokenMap(deducedPlayerTokenMap);

		// get forest,mountain, beach only tokens from deduced map
		Set<String> deducedForestLoc = new HashSet<>();
		Set<String> deducedBeachLoc = new HashSet<>();
		Set<String> deducedMountainLoc = new HashSet<>();
		for (String terrain : deducedPlayerTokenMap.keySet()) {

			if (terrain.contains(Constants.FOREST_CHAR)) {
				deducedForestLoc.add(terrain);
			} else if (terrain.contains(Constants.BEACH_CHAR)) {
				deducedBeachLoc.add(terrain);
			} else if (terrain.contains(Constants.MOUNTAINS_CHAR)) {
				deducedMountainLoc.add(terrain);
			}

		}
		ComputerPlayer.getInstance().setDeducedForestLoc(deducedForestLoc);
		ComputerPlayer.getInstance().setDeducedBeachLoc(deducedBeachLoc);
		ComputerPlayer.getInstance().setDeducedMountainLoc(deducedMountainLoc);
		return deducedPlayerTokenMap;
	}

	/**
	 * This function is used to check we found treasure location or not
	 * 
	 * @return
	 */
	public static String checkIsTreasureLocFound() {
		if (ComputerPlayer.getInstance().getTreasureLoc() != null
				&& ComputerPlayer.getInstance().getTreasureLoc().size() == 2)
			return Constants.YES;
		else if (ComputerPlayer.getInstance().getNotTreasureLoc() != null
				&& ComputerPlayer.getInstance().getNotTreasureLoc().size() == 22) {
			Set<String> allLocation = ComputerPlayer.getInstance().getAllPlayerTrrianMap().keySet();
			allLocation.removeAll(ComputerPlayer.getInstance().getNotTreasureLoc());
			ComputerPlayer.getInstance().getNotTreasureLoc().addAll(allLocation);
			return Constants.YES;
		}
		return Constants.NO;

	}

	/**
	 * This function used to update terrain map for not treasure location
	 * 
	 * @param updatedterrainList
	 * @param playerName
	 */
	public static void updateterrainMapForNotTreasureLoc(List<String> updatedterrainList, String playerName) {
		for (String terrainToken : updatedterrainList) {
			Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrainToken);
			terrainMap.put(playerName, 0);
			ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken, terrainMap);
		}
	}
}
