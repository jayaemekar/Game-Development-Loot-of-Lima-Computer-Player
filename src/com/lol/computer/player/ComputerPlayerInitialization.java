package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

/*
 * This class is used to initialize the computer player
 */
public class ComputerPlayerInitialization {
	private static Integer count = 1;
	private static Integer countTen = 0;

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

	public static void updateAllPersonalTokenMap(List<String> messageDetailsList) {

		String noOfTokens = messageDetailsList.get(3);
		String playerName = messageDetailsList.get(4);
		String Diretion1 = messageDetailsList.get(0).substring(0, 2);
		String Diretion2 = messageDetailsList.get(1).substring(0, 2);
		int tokenCount = Integer.valueOf(noOfTokens);
		Node head = ComputerPlayer.getInstance().createNode(Diretion1);
		Node tail = ComputerPlayer.getInstance().createNode(Diretion2);
		Set<String> zeroTerrain = new HashSet<>();
		Set<String> oneTerrain = new HashSet<>();
		Set<String> tenTerrain = new HashSet<>();
		List<Set<String>> threeSet = new ArrayList<>();
		threeSet.add(zeroTerrain);
		threeSet.add(oneTerrain);
		threeSet.add(tenTerrain);

		Map<String, List<List<String>>> allTentativeToken = new HashMap<>();
		List<String> PlayerList1 = PlayerInformation.getInstance().getPlayerNameList();
		for (String player : PlayerList1) {
			allTentativeToken.put(player, new ArrayList<List<String>>());
		}
		ComputerPlayer.getInstance().setAllTentativeToken(allTentativeToken);

		if (head.direction.equals(tail.direction)) {

			getTerrainStatus(ComputerPlayer.getInstance().getAllPlayerTrrianMap(), threeSet, playerName);

		} else {
			while (head != tail) {

				getTerrainStatus(head.terrainList, threeSet, playerName);

				head = head.next;
			}
		}

		if (threeSet.get(1).size() > 0) {

			tokenCount = tokenCount - threeSet.get(1).size();

		}

		if (tokenCount > 0) {
			if (threeSet.get(2).size() > tokenCount) {

				List<String> value = new ArrayList<>();
				value.add(Diretion1);
				value.add(Diretion2);
				value.add(Integer.toString(tokenCount));

				if (ComputerPlayer.getInstance().getAllTentativeToken().containsKey(playerName)) {

					List<List<String>> list = ComputerPlayer.getInstance().getAllTentativeToken().get(playerName);
					list.add(value);

				} else {
					List<List<String>> list2 = new ArrayList<>();
					list2.add(value);
					ComputerPlayer.getInstance().getAllTentativeToken().put(playerName, list2);

				}

			}

			if (threeSet.get(2).size() == tokenCount) {

				List<String> PlayerList = PlayerInformation.getInstance().getPlayerNameList();
				for (String player : PlayerList) {
					// marking 1 for having the token

					for (String terrainToken : tenTerrain) {
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
		}

	}

	public static List<Set<String>> getTerrainStatus(Map<String, Map<String, Integer>> map, List<Set<String>> threeSet,
			String playerName) {

		map.entrySet().stream().forEach(terrain -> {

			terrain.getValue().entrySet().stream().forEach(playerMap -> {
				if (playerMap.getKey().equals(playerName)) {
					if (playerMap.getValue() == 0) {
						threeSet.get(0).add(terrain.getKey());
					}
					if (playerMap.getValue() == 1) {
						threeSet.get(1).add(terrain.getKey());
					}
					if (playerMap.getValue() == -1) {

						threeSet.get(2).add(terrain.getKey());
					}
				}

			});
		});

		return threeSet;

	}

	public static void checkTentativeTerrain(String direction1) {

		System.out.println("start checkTentativeTerrain" + ComputerPlayer.getInstance().getAllTentativeToken());
		Set<String> tenTerrain = new HashSet<>();

		if (ComputerPlayer.getInstance().getAllTentativeToken() != null) {
			ComputerPlayer.getInstance().getAllTentativeToken().entrySet().stream().forEach(player -> {

				List<List<String>> list = player.getValue();
				list.forEach(value -> {
					if (value.get(0).equals(direction1)) {

						Node head = ComputerPlayer.getInstance().createNode(direction1);

						Node tail = ComputerPlayer.getInstance().createNode(value.get(1));

						while (head != tail) {
							head.terrainList.entrySet().stream().forEach(terrain -> {

								terrain.getValue().entrySet().stream()
										.filter(playerMap -> playerMap.getKey().equals(player.getKey()))
										.forEach(playerMap -> {

											if (playerMap.getValue() == -1) {
												tenTerrain.add(terrain.getKey());
												countTen = countTen++;
											}

										});
							});
							head = head.next;
						}

						if (value.get(2).equals(Integer.toString(countTen))) {

							List<String> PlayerList = PlayerInformation.getInstance().getPlayerNameList();
							for (String player1 : PlayerList) {
								// marking 1 for having the token

								for (String terrainToken : tenTerrain) {
									Map<String, Integer> terrainMap = ComputerPlayer.getInstance()
											.getAllPlayerTrrianMap().get(terrainToken);
									if (terrainMap != null) {
										if (!player.getKey().equals(player1))
											terrainMap.put(player1, 0);
										else
											terrainMap.put(player1, 1);
										ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken,
												terrainMap);
									}
								}
							}
						}

						if ((countTen) > Integer.parseInt(value.get(2))) {

						}

					}

				});
			});
		}

		System.out.println("outside checkTentativeTerrain");
	}

}