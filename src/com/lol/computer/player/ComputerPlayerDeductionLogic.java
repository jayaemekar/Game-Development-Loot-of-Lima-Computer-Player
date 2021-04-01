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
	 * this method is to get the answer information 06:NNF,NEF,F,2,P3,P1
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public static void getAnswerInformation(String messageNumber, List<String> messageDetailsList) {
		for (String terrain : ComputerPlayer.getInstance().getAllPlayerTrrianMap().keySet()) {
			Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain);
			int sum = terrainMap.values().stream().reduce(0, Integer::sum);
			if (sum == 1) {
				Set<String> treasureNotLocSet = ComputerPlayer.getInstance().getNotTreasureLoc();
				treasureNotLocSet.add(terrain);
				ComputerPlayer.getInstance().setNotTreasureLoc(treasureNotLocSet);
			}
		}
		System.out.println(
				"Number of non Treasure location count ::  " + ComputerPlayer.getInstance().getNotTreasureLoc().size());
		System.out.println("Treasure Location identified  ::  " + ComputerPlayer.getInstance().getTreasureLoc());
		checkIsTreasureLocFound();
		System.out.println("Message [" + messageNumber + "] Player " + messageDetailsList.get(4) + " has "
				+ messageDetailsList.get(3) + " "
				+ PlayerInformation.getInstance().getTerrianTokenInformation(messageDetailsList.get(2))
				+ " terrain between "
				+ PlayerInformation.getInstance().getDirectionInformation(messageDetailsList.get(0)) + " and "
				+ PlayerInformation.getInstance().getDirectionInformation(messageDetailsList.get(1)) + "\n");
		if (!PlayerInformation.getInstance().getPlayerName().equals(messageDetailsList.get(4)))
			ComputerPlayerDeductionLogic.processAnswerMessage(messageDetailsList);

	}

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

		// deduce terrain locations map based on answer given by player
		if (terrainToken != null && !terrainToken.isEmpty()) {

			updateDeducedterrainMap(deducedterrainToken, terrainToken, playerName,
					PlayerInformation.getInstance().getPlayerNameList(), messageDetailsList, Diretion1);
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
	 * @param diretion1
	 */
	public static void updateDeducedterrainMap(Set<String> deducedterrainToken, Set<String> terrainToken,
			String playerName, List<String> playerList, List<String> messageDetailsList, String diretion1) {
		Integer noIfTokens = Integer.valueOf(messageDetailsList.get(3));
		String areaToken = messageDetailsList.get(2);

		Set<String> deducedBeachLoc = ComputerPlayer.getInstance().getDeducedBeachLoc();
		Set<String> deducedForestLoc = ComputerPlayer.getInstance().getDeducedForestLoc();
		Set<String> deducedMountainLoc = ComputerPlayer.getInstance().getDeducedMountainLoc();
		Set<String> deducedAllTokenLoc = ComputerPlayer.getInstance().getDeducedPlayerTokenMap().keySet();

		Set<String> totBeachLoc = new HashSet<>();
		Set<String> totForestLoc = new HashSet<>();
		Set<String> totMountainLoc = new HashSet<>();

		for (String terrain : terrainToken) {
			if (ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain)
					.get(PlayerInformation.getInstance().getPlayerName()) != 1) {
				if (terrain.contains(Constants.BEACH_CHAR))
					totBeachLoc.add(terrain);
				else if (terrain.contains(Constants.FOREST_CHAR))
					totForestLoc.add(terrain);
				else if (terrain.contains(Constants.MOUNTAINS_CHAR))
					totMountainLoc.add(terrain);
			}
		}

		if (noIfTokens == 0) {
			if (Constants.BEACH_CHAR.equals(areaToken)) {
				System.out.println("terrain needs to be processed for beach terrain deduction : " + deducedBeachLoc);
				updateZeroterrainTokenInformation(deducedBeachLoc, playerName, terrainToken);
			} else if (Constants.FOREST_CHAR.equals(areaToken)) {
				System.out.println("terrain needs to be processed for forest terrain deduction : " + deducedForestLoc);
				updateZeroterrainTokenInformation(deducedForestLoc, playerName, terrainToken);
			} else if (Constants.MOUNTAINS_CHAR.equals(areaToken)) {
				System.out.println(
						"terrain needs to be processed for mountain terrain deduction : " + deducedMountainLoc);
				updateZeroterrainTokenInformation(deducedMountainLoc, playerName, terrainToken);
			} else {
				System.out.println("terrain needs to be processed for all terrain deduction : " + deducedAllTokenLoc);
				updateZeroterrainTokenInformation(deducedAllTokenLoc, playerName, terrainToken);
			}
		} else {
			
			ComputerPlayerInitialization.updateAllPersonalTokenMap(messageDetailsList);
			ComputerPlayerInitialization.checkTentativeTerrain1(diretion1);
//			if (Constants.BEACH_CHAR.equals(areaToken)) {
//				updateTerrainMapForNonZeroInformation(noIfTokens, totBeachLoc, deducedBeachLoc, playerName,
//						terrainToken);
//			} else if (Constants.FOREST_CHAR.equals(areaToken)) {
//				updateTerrainMapForNonZeroInformation(noIfTokens, totForestLoc, deducedForestLoc, playerName,
//						terrainToken);
//			} else if (Constants.MOUNTAINS_CHAR.equals(areaToken)) {
//				updateTerrainMapForNonZeroInformation(noIfTokens, totMountainLoc, deducedMountainLoc, playerName,
//						terrainToken);
//			}
		}

		if (Constants.ALL_CHAR.equals(areaToken)) {
			ComputerPlayerInitialization.updateAllPersonalTokenMap(messageDetailsList);
			ComputerPlayerInitialization.checkTentativeTerrain(diretion1);
		}
	}

	private static void updateTerrainMapForNonZeroInformation(Integer noIfTokens, Set<String> totalLoc,
			Set<String> deducedLoc, String playerName, Set<String> terrainToken) {
		if (noIfTokens == totalLoc.size()) {
			System.out.println("terrain needs to be processed for beach terrain deduction : " + deducedLoc);
			ComputerPlayerInitialization.updatePersonalTokenMap(deducedLoc.stream().collect(Collectors.toList()),
					playerName, PlayerInformation.getInstance().getPlayerNameList());
		} else if (noIfTokens < totalLoc.size()) {
			Set<String> totalLocForZero = totalLoc;
			for (String terrain : totalLoc) {
				if (ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain).get(playerName) == 0) {
					totalLocForZero.remove(ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain));
				}
			}
			if (noIfTokens == totalLocForZero.size()) {
				ComputerPlayerInitialization.updatePersonalTokenMap(
						totalLocForZero.stream().collect(Collectors.toList()), playerName,
						PlayerInformation.getInstance().getPlayerNameList());

			}
			Set<String> totalLocForNonZero = totalLoc;
			for (String terrain : totalLoc) {
				if (ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain).get(playerName) == 1) {
					totalLocForNonZero.remove(ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrain));
				}
			}
			if (noIfTokens == totalLocForNonZero.size()) {
				updateZeroterrainTokenInformation(totalLocForNonZero, playerName, terrainToken);

			}
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
			} else if (sum == 1) {
				Set<String> treasureNotLocSet = ComputerPlayer.getInstance().getNotTreasureLoc();
				treasureNotLocSet.add(terrain);
				ComputerPlayer.getInstance().setNotTreasureLoc(treasureNotLocSet);
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

			Set<String> treasureLocSet = new HashSet<>();
			treasureLocSet.addAll(allLocation);
			ComputerPlayer.getInstance().setTreasureLoc(treasureLocSet);
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
