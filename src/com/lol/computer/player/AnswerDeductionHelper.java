package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.omg.Messaging.SyncScopeHelper;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class AnswerDeductionHelper {

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

	static void checkTentativeTerrain() {

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

	public static void checkAllToken() {

		Map<Integer, Set<String>> areaTokenSet = new HashMap<>();
		areaTokenSet.put(Constants.NOT_WITH_PLAYER_TERRAIN, new HashSet<>());
		areaTokenSet.put(Constants.CONFIRM_TERRAIN, new HashSet<>());
		areaTokenSet.put(Constants.TENTATIVE_TERRAIN, new HashSet<>());
		System.out.println("in all check");
		for (String player : PlayerInformation.getInstance().getPlayerNameList()) {

			AnswerDeductionHelper.getTerrainStatus(ComputerPlayer.getInstance().getAllPlayerTrrianMap(), areaTokenSet,
					player);

			areaTokenSet.entrySet().stream().forEach(tera -> {

				tera.getValue().forEach(ter -> {

					if (tera.getKey() != 0) {
						checkAllTokensWithDirection(ter, tera);
					}
//					System.out.println("ComputerPlayer.getInstance().getWestSet()"+ ComputerPlayer.getInstance().getWestSet());
				});
			});
		}

		Map<String, Map<String, List<List<String>>>> tentativetoken = createTentativeTokenMap();
		System.out.println(ComputerPlayer.getInstance().getTentativeToken());
		System.out.println(tentativetoken);
		if (tentativetoken != null) {
			Map<String, List<List<String>>> token = updateTentativeTokenMap(tentativetoken);
			System.out.println("token" + token);
			updateFinalSet(token);
		}

	}

	private static void updateFinalSet(Map<String, List<List<String>>> token) {
		HashSet<String> finalSet = new HashSet<>();
		HashSet<String> sureSet = new HashSet<>();
		token.entrySet().stream().forEach(to -> {

			to.getValue().forEach(value1 -> {

				Node head = ComputerPlayer.getInstance().createNode(value1.get(0));
				Node tail = ComputerPlayer.getInstance().createNode(value1.get(1));
				if (head.direction.equals(tail.direction))
					getLocationForEachDirection(value1.get(0), to.getKey(), finalSet, sureSet);
				else {

					while (head != tail) {
						System.out.println(head.direction);
						getLocationForEachDirection(head.direction, to.getKey(), finalSet, sureSet);
						head = head.next;
					}
				}
				
				PlayerInformation.getInstance().getPersonalTokenMap().entrySet().forEach(pertoken->{
					sureSet.removeAll(pertoken.getValue());
					
				});
				int count = finalSet.size() + sureSet.size();
				System.out.println(finalSet.size());
				System.out.println(sureSet.size());
				
				if (Integer.toString(count).equals(value1.get(2))) {
					System.out.println("in sure");
					finalSet.removeAll(ComputerPlayer.getInstance().getNotTreasureLoc());
					finalSet.forEach(tres -> {
						ComputerPlayer.getInstance().getNotTreasureLoc().add(tres);

					});
				}
				finalSet.clear();
				sureSet.clear();
			});
		});
	}

	private static Map<String, List<List<String>>> updateTentativeTokenMap(
			Map<String, Map<String, List<List<String>>>> tentativetoken) {
		Map<String, List<List<String>>> token = new HashMap<>();
		Map<String, List<List<String>>> map2 = new  HashMap<>();
		tentativetoken.entrySet().stream().forEach(value -> {

			value.getValue().entrySet().stream().forEach(direc -> {

				direc.getValue().forEach(val -> {
					List<String> list = new ArrayList<>();

					if (token.containsKey(direc.getKey())) {
						for (List<String> y1 : token.get(direc.getKey())) {
							
							if (y1.get(0).equals(val.get(0)) && y1.get(1).equals(val.get(1))) {
								
								System.out.println("y1" + y1);
								y1.set(2, Integer.toString(Integer.parseInt(y1.get(2)) + Integer.parseInt(val.get(2))));
								
								list.addAll(y1);
								if(map2.containsKey(direc.getKey())) {
									map2.get(direc.getKey()).add(list);
								}
								else {
									map2.put(direc.getKey(), new ArrayList<>());
									map2.get(direc.getKey()).add(list);
								}
								System.out.println(list);
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
		System.out.println(token);
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

	private static void checkAllTokensWithDirection(String ter, Entry<Integer, Set<String>> tera) {
		if (Character.toString(ter.charAt(0)).equals(Constants.NN)) {

			if (!ComputerPlayer.getInstance().getNorthSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getNorthSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getNorthSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.NE)) {

			if (!ComputerPlayer.getInstance().getNorthEastSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getNorthEastSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getNorthEastSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.EE)) {

			if (!ComputerPlayer.getInstance().getEastSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getEastSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getEastSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.SE)) {

			if (!ComputerPlayer.getInstance().getSouthEastSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getSouthEastSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getSouthEastSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.SS)) {

			if (!ComputerPlayer.getInstance().getSouthSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getSouthSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getSouthSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.SW)) {

			if (!ComputerPlayer.getInstance().getSouthWestSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getSouthWestSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getSouthWestSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.WW)) {

			if (!ComputerPlayer.getInstance().getWestSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getWestSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getWestSet().get(tera.getKey()).add(ter);
		}
		if (Character.toString(ter.charAt(0)).equals(Constants.NW)) {

			if (!ComputerPlayer.getInstance().getNorthWestSet().containsKey(tera.getKey()))
				ComputerPlayer.getInstance().getNorthWestSet().put(tera.getKey(), new HashSet<>());
			else
				ComputerPlayer.getInstance().getNorthWestSet().get(tera.getKey()).add(ter);
		}
	}

//	private static Set<String> getLocation(String location, String terrain, HashSet<String> finalSet,
//			HashSet<String> sureSet) {
//
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.NORTH_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.NORTH_EAST_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.EAST_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.SOUTH_EAST_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.SOUTH_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.SOUTH_WEST_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.WEST_CHAR);
//		getLocationForEachDirection(location, terrain, finalSet, sureSet, Constants.NORTH_WEST_CHAR);
//
//		return finalSet;
//
//	}

	private static void getLocationForEachDirection(String location, String terrain, HashSet<String> finalSet,
			HashSet<String> sureSet) {
		if (location.equals(Constants.NORTH_CHAR)) {
			ComputerPlayer.getInstance().getNorthSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.NORTH_EAST_CHAR)) {
			ComputerPlayer.getInstance().getNorthEastSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.EAST_CHAR)) {
			ComputerPlayer.getInstance().getEastSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.SOUTH_EAST_CHAR)) {
			ComputerPlayer.getInstance().getSouthEastSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.SOUTH_CHAR)) {
			ComputerPlayer.getInstance().getSouthSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.SOUTH_WEST)) {
			ComputerPlayer.getInstance().getSouthWestSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.WEST_CHAR)) {
			ComputerPlayer.getInstance().getWestSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
		if (location.equals(Constants.NORTH_WEST_CHAR)) {
			ComputerPlayer.getInstance().getNorthWestSet().entrySet().stream().forEach(value1 -> {
				value1.getValue().forEach(value -> {

					if (value1.getKey() == Constants.TENTATIVE_TERRAIN)
						finalSet.add(value);
					if(value1.getKey() == Constants.CONFIRM_TERRAIN)
						sureSet.add(value);
				});
			});
		}
	}

}
