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
		String Diretion1 = messageDetailsList.get(0);
		String Diretion2 = messageDetailsList.get(1);
		Integer dirNum1 = ComputerPlayer.getInstance().getDirIntMap().get(Diretion1.substring(0, 2));
		Integer dirNum2 = ComputerPlayer.getInstance().getDirIntMap().get(Diretion2.substring(0, 2));
		// get all the terrain tokens present between the two selected direction
		Set<String> terrianToken = getTokenBetweenDirection(dirNum1, dirNum2);
		// remove already identified non treasure location from terrain token
		// Map present between the two selected direction as deduced map
		Set<String> deducedTerrainToken = updateDeducedPlayerTokenMap(dirNum1, dirNum2, terrianToken).keySet();
		// Get intersection of terrainToken and deducedTerrainToken
		terrianToken.retainAll(deducedTerrainToken);
		System.out.println("Terrain needs to be processed : " + terrianToken);

		// deduce terrain locations map based on answer given by player
		if (terrianToken != null && !terrianToken.isEmpty()) {
			updateDeducedTerrainMap(deducedTerrainToken, terrianToken, playerName,
					PlayerInformation.getInstance().getPlayerNameList(), messageDetailsList);

		}
		// after answer processing check is treasure location found or not
		checkIsTreasureLocFound();
	}

	/**
	 * This method is used to find out the the terrain location between two
	 * direction
	 * 
	 * @param dirNum1
	 * @param dirNum2
	 * @return
	 */
	private static Set<String> getTokenBetweenDirection(Integer dirNum1, Integer dirNum2) {
		Set<String> terrainToken = new HashSet<>();
		if (dirNum1 > dirNum2) {
			for (int i = dirNum1; i <= 8; i++)
				terrainToken.addAll(ComputerPlayer.getInstance().getDirectionIntegerMap().get(i));
			for (int i = 1; i < dirNum2; i++)
				terrainToken.addAll(ComputerPlayer.getInstance().getDirectionIntegerMap().get(i));
		} else {
			for (int i = dirNum1; i < dirNum2; i++)
				terrainToken.addAll(ComputerPlayer.getInstance().getDirectionIntegerMap().get(i));
		}
		return terrainToken;
	}

	/**
	 * This method is used to update the map with the deduced terrains
	 * 
	 * @param deducedTerrainToken
	 * @param terrainToken
	 * @param playerName
	 * @param playerList
	 * @param messageDetailsList
	 */
	public static void updateDeducedTerrainMap(Set<String> deducedTerrainToken, Set<String> terrainToken,
			String playerName, List<String> playerList, List<String> messageDetailsList) {
		String noIfTokens = messageDetailsList.get(3);
		String areaToken = messageDetailsList.get(2);
		/*
		 * Case 1: Update deduce map when we receive player has zero terrain in
		 * given direction 06:NNF,NEF,F,0,P3,P2 06:NNF,NEF,M,0,P3,P2
		 * 06:NNF,NEF,B,0,P3,P2 06:NNF,NEF,A,0,P3,P2 Marking the location in
		 * deduced map for the player who answered question as
		 */
		if (Integer.valueOf(noIfTokens) == 0) {

			if (Constants.BEACH_CHAR.equals(areaToken)) {
				Set<String> deducedBeachLoc = ComputerPlayer.getInstance().getDeducedBeachLoc();
				updateZeroTerrainTokenInformation(deducedBeachLoc, playerName, terrainToken);
			} else if (Constants.FOREST_CHAR.equals(areaToken)) {
				Set<String> deducedForestLoc = ComputerPlayer.getInstance().getDeducedForestLoc();
				updateZeroTerrainTokenInformation(deducedForestLoc, playerName, terrainToken);
			} else if (Constants.MOUNTAINS_CHAR.equals(areaToken)) {
				Set<String> deducedMountainLoc = ComputerPlayer.getInstance().getDeducedMountainLoc();
				updateZeroTerrainTokenInformation(deducedMountainLoc, playerName, terrainToken);
			} else {
				Set<String> deducedAllTokenLoc = ComputerPlayer.getInstance().getDeducedPlayerTokenMap().keySet();
				updateZeroTerrainTokenInformation(deducedAllTokenLoc, playerName, terrainToken);
			}
		} else {

			/*
			 * Case 2: Update deduce map when we receive player has zero terrain
			 * in given direction 06:NNF,NEF,F,1,P3,P2 06:NNF,NEF,M,2,P3,P2
			 * 06:NNF,NEF,3,0,P3,P2 Marking the location in deduced map for the
			 * player who answered question as
			 */
			if (Constants.BEACH_CHAR.equals(areaToken) && Integer.valueOf(noIfTokens) == terrainToken.size()) {
				ComputerPlayerIntialization.updatePersonalTokenMap(terrainToken.stream().collect(Collectors.toList()),
						playerName, PlayerInformation.getInstance().getPlayerNameList());
			} else if (Constants.MOUNTAINS_CHAR.equals(areaToken)
					&& Integer.valueOf(noIfTokens) == terrainToken.size()) {
				ComputerPlayerIntialization.updatePersonalTokenMap(terrainToken.stream().collect(Collectors.toList()),
						playerName, PlayerInformation.getInstance().getPlayerNameList());

			} else if (Constants.FOREST_CHAR.equals(areaToken) && Integer.valueOf(noIfTokens) == terrainToken.size()) {
				ComputerPlayerIntialization.updatePersonalTokenMap(terrainToken.stream().collect(Collectors.toList()),
						playerName, PlayerInformation.getInstance().getPlayerNameList());

			} else if (Constants.ALL_CHAR.equals(areaToken) && Integer.valueOf(noIfTokens) == terrainToken.size()) {
				// Need to identify the logic for this type of message.
			}
		}

	}

	/**
	 * Mark remaining tokens of playing player as zero
	 */
	private static void updateZeroTerrainTokenInformation(Set<String> deducedTerrainLoc, String playerName,
			Set<String> terrainToken) {
		List<String> updatedTerrainList = new ArrayList<>();
		for (String terrain : terrainToken) {
			if (deducedTerrainLoc.contains(terrain)) {
				updatedTerrainList.add(terrain);
				updateTerrainMapForNotTreasureLoc(updatedTerrainList, playerName);
			}
		}
	}

	/**
	 * create and update deduced player token map depending upon given direction
	 * 
	 * @param dirNum1
	 * @param dirNum2
	 * @param terrainDirectionMap
	 * @return
	 */
	public static Map<String, Map<String, Integer>> updateDeducedPlayerTokenMap(Integer dirNum1, Integer dirNum2,
			Set<String> terrainDirectionMap) {

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
	 * @param updatedTerrainList
	 * @param playerName
	 */
	public static void updateTerrainMapForNotTreasureLoc(List<String> updatedTerrainList, String playerName) {
		for (String terrainToken : updatedTerrainList) {
			Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrainToken);
			terrainMap.put(playerName, 0);
			ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken, terrainMap);
		}
	}
}
