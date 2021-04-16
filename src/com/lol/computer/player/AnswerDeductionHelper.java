package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class AnswerDeductionHelper {
	/**
	 * This method is used to get terrain status
	 * 
	 * @param map
	 * @param areaTokenSet
	 * @param playerName
	 * @return
	 */
	public static Map<Integer, Set<String>> getTerrainStatus(Map<String, Map<String, Integer>> map,
			Map<Integer, Set<String>> areaTokenSet, String playerName) {

		map.entrySet().stream().forEach(terrain -> {

			terrain.getValue().entrySet().stream().forEach(playerMap -> {
				if (playerMap.getKey().equals(playerName)) {
					if (playerMap.getValue() == Constants.NOT_WITH_PLAYER_TERRAIN)
						areaTokenSet.get(Constants.NOT_WITH_PLAYER_TERRAIN).add(terrain.getKey());

					else if (playerMap.getValue() == Constants.CONFIRM_TERRAIN)
						areaTokenSet.get(Constants.CONFIRM_TERRAIN).add(terrain.getKey());

					else if (playerMap.getValue() == Constants.TENTATIVE_TERRAIN)
						areaTokenSet.get(Constants.TENTATIVE_TERRAIN).add(terrain.getKey());
				}
			});
		});

		return areaTokenSet;
	}

	/**
	 * This method is used to update terrain token map
	 * 
	 * @param tenTerrain
	 * @param player
	 */
	public static void updateTerrainTokenMap(Set<String> tenTerrain, String player) {

		List<String> PlayerList = PlayerInformation.getInstance().getPlayerNameList();
		for (String player1 : PlayerList) {
			// marking 1 for having the token
			for (String terrainToken : tenTerrain) {
				Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap()
						.get(terrainToken);
				if (terrainMap != null) {
					if (!player.equals(player1))
						terrainMap.put(player1, Constants.NOT_WITH_PLAYER_TERRAIN);
					else
						terrainMap.put(player1, Constants.CONFIRM_TERRAIN);

					ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken, terrainMap);
				}
			}
		}
		checkTentativeTerrain();
	}

	/**
	 * This method is used to check tentative terrain
	 */
	protected static void checkTentativeTerrain() {

		Map<String, Map<String, List<List<String>>>> TentativeToken = ComputerPlayer.getInstance().getTentativeToken();
		Map<String, List<List<String>>> Map = new HashMap<>();

		if (ComputerPlayer.getInstance().getTentativeToken() != null) {

			ComputerPlayer.getInstance().getTentativeToken().entrySet().stream().forEach(player -> {

				player.getValue().entrySet().stream().forEach(location -> {

					location.getValue().forEach(block -> {

						Map<Integer, Set<String>> areaTokenSet = new HashMap<>();
						areaTokenSet.put(Constants.NOT_WITH_PLAYER_TERRAIN, new HashSet<>());
						areaTokenSet.put(Constants.CONFIRM_TERRAIN, new HashSet<>());
						areaTokenSet.put(Constants.TENTATIVE_TERRAIN, new HashSet<>());

						Node head = ComputerPlayer.getInstance().createNode(block.get(0));
						Node tail = ComputerPlayer.getInstance().createNode(block.get(1));
						if (head.direction.equals(tail.direction))
							AnswerDeductionHelper.getTerrainStatus(ComputerPlayer.getInstance().getAllPlayerTrrianMap(),
									areaTokenSet, player.getKey());
						else
							while (head != tail) {
								AnswerDeductionHelper.getTerrainStatus(head.terrainList, areaTokenSet, player.getKey());
								head = head.next;
							}

						if (Constants.BEACH_CHAR.equals(location.getKey()))
							AnswerDeductionLogic.updateAreaTokenSet(location.getKey(), areaTokenSet);
						else if (Constants.FOREST_CHAR.equals(location.getKey()))
							AnswerDeductionLogic.updateAreaTokenSet(location.getKey(), areaTokenSet);
						else if (Constants.MOUNTAINS_CHAR.equals(location.getKey()))
							AnswerDeductionLogic.updateAreaTokenSet(location.getKey(), areaTokenSet);

						List<List<String>> value2 = AnswerDeductionLogic.updateStatus(areaTokenSet, location.getKey(),
								Integer.parseInt(block.get(2)), block.get(0), block.get(1), player.getKey());

						if (!Map.containsKey(location.getKey())) {
							Map.put(location.getKey(), value2);
						} else {
							Map.remove(location.getKey());
							Map.put(location.getKey(), value2);
						}
					});

					if (!(TentativeToken.containsKey(player.getKey())))
						TentativeToken.put(player.getKey(), Map);

				});

				Map.clear();
			});

			ComputerPlayer.getInstance().setTentativeToken(TentativeToken);
		}
	}

	/**
	 * This method is used to check and update the map for ALL terrain answers
	 */
	public static void checkAllToken() {

		Map<String, Map<String, List<List<String>>>> tentativetoken = createTentativeTokenMap();
		if (tentativetoken != null) {
			Map<String, List<List<String>>> token = updateTentativeTokenMap(tentativetoken);
			updateFinalSet(token);
		}

	}

	/**
	 * This method is used to mark the tokens after processing
	 * 
	 * @param token
	 */
	private static void updateFinalSet(Map<String, List<List<String>>> token) {
		HashSet<String> finalSet = new HashSet<>();
		HashSet<String> sureSet = new HashSet<>();
		token.entrySet().stream().forEach(to -> {

			to.getValue().forEach(value1 -> {

				Node head = ComputerPlayer.getInstance().createNode(value1.get(0));
				Node tail = ComputerPlayer.getInstance().createNode(value1.get(1));

				do {

					head.terrainList.entrySet().forEach(terrainVal -> {
						terrainVal.getValue().entrySet().forEach(playerTerrain -> {

							if (playerTerrain.getValue() == Constants.TENTATIVE_TERRAIN)
								finalSet.add(terrainVal.getKey());
							if (playerTerrain.getValue() == Constants.CONFIRM_TERRAIN)
								sureSet.add(terrainVal.getKey());
						});
					});

					head = head.next;
				} while ((head != tail));

				PlayerInformation.getInstance().getPersonalTokenMap().entrySet().forEach(pertoken -> {
					sureSet.removeAll(pertoken.getValue());

				});
				int count = finalSet.size() + sureSet.size();

				if (Integer.toString(count).equals(value1.get(2))) {
					finalSet.removeAll(ComputerPlayer.getInstance().getNotTreasureLoc());
					finalSet.forEach(tres -> {
						ComputerPlayer.getInstance().getNotTreasureLoc().add(tres);
					});
				}
				finalSet.clear();
				sureSet.clear();
			});
		});
		AnswerDeductionLogic.checkIsTreasureLocFound();

	}

	/**
	 * This method is to update the tentative Map
	 * 
	 * @param tentativetoken
	 * @return
	 */
	private static Map<String, List<List<String>>> updateTentativeTokenMap(
			Map<String, Map<String, List<List<String>>>> tentativetoken) {
		Map<String, List<List<String>>> token = new HashMap<>();
		Map<String, List<List<String>>> map2 = new HashMap<>();
		tentativetoken.entrySet().stream().forEach(value -> {

			value.getValue().entrySet().stream().forEach(direc -> {

				direc.getValue().forEach(val -> {
					List<String> list = new ArrayList<>();

					if (token.containsKey(direc.getKey())) {
						for (List<String> y1 : token.get(direc.getKey())) {

							if (y1.get(0).equals(val.get(0)) && y1.get(1).equals(val.get(1))) {

								y1.set(2, Integer.toString(Integer.parseInt(y1.get(2)) + Integer.parseInt(val.get(2))));

								list.addAll(y1);
								if (map2.containsKey(direc.getKey())) {
									map2.get(direc.getKey()).add(list);
								} else {
									map2.put(direc.getKey(), new ArrayList<>());
									map2.get(direc.getKey()).add(list);
								}
							}
						}
						token.get(direc.getKey()).add(val);
					}

					else {
						List<List<String>> token3 = new ArrayList<>();
						token3.add(val);
						token.put(direc.getKey(), token3);
					}
				});
			});
		});
		return map2;
	}

	private static Map<String, Map<String, List<List<String>>>> createTentativeTokenMap() {
		Map<String, Map<String, List<List<String>>>> tentativetoken = new HashMap<String, Map<String, List<List<String>>>>();
		if (ComputerPlayer.getInstance().getTentativeToken() != null) {
			ComputerPlayer.getInstance().getTentativeToken().entrySet().stream().forEach(play -> {
				tentativetoken.put(play.getKey(), new HashMap<>());
				play.getValue().entrySet().stream().forEach(terr -> {
					List<List<String>> list = new ArrayList<>();

					terr.getValue().forEach(loc -> {
						List<String> listTemp = new ArrayList<>();
						listTemp.add(loc.get(0));
						listTemp.add(loc.get(1));
						listTemp.add(loc.get(2));
						list.add(listTemp);

					});

					tentativetoken.get(play.getKey()).put(terr.getKey(), list);

				});

			});
		}
		return tentativetoken;
	}

}
