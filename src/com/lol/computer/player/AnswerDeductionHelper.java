package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.omg.Messaging.SyncScopeHelper;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class AnswerDeductionHelper {

	
	
	public static Map<Integer, Set<String>> getTerrainStatus(Map<String, Map<String, Integer>> map, Map<Integer , Set<String>> areaTokenSet, 
			String playerName) {
		
		

		map.entrySet().stream().forEach(terrain -> {

			terrain.getValue().entrySet().stream().forEach(playerMap -> {
				if (playerMap.getKey().equals(playerName)) {
					if (playerMap.getValue() == 0) {
						areaTokenSet.get(0).add(terrain.getKey());
						
					}
					if (playerMap.getValue() == 1) {
						areaTokenSet.get(1).add(terrain.getKey());
					}
					if (playerMap.getValue() == -1) {
						areaTokenSet.get(-1).add(terrain.getKey());
				}}
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
						terrainMap.put(player1, 0);
					else
						terrainMap.put(player1, 1);

					ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken, terrainMap);
				}
			}
		}
		System.out.println("in update" + ComputerPlayer.getInstance().getTentativeToken());
		checkTentativeTerrain();
	}


	private static void checkTentativeTerrain() {
		// TODO Auto-generated method stub
		List<List<String>> value = new ArrayList<>();
		Map<String, Map<String, List<List<String>>>> TentativeToken = ComputerPlayer.getInstance().getTentativeToken();
		Map<String, List<List<String>>> Map = new HashMap<>();
		
		if (ComputerPlayer.getInstance().getTentativeToken() != null) {
			System.out.println("in");
			ComputerPlayer.getInstance().getTentativeToken().entrySet().stream().forEach(player -> {
				
				
				player.getValue().entrySet().stream().forEach(location -> {
					System.out.println(player.getKey());
					
					

					location.getValue().forEach(block -> {
						System.out.println("in");
						Map<Integer , Set<String>> areaTokenSet = new HashMap<>();
						areaTokenSet.put(0, new HashSet<>());
						areaTokenSet.put(1, new HashSet<>());
						areaTokenSet.put(-1, new HashSet<>());
						
						Node head = ComputerPlayer.getInstance().createNode(block.get(0));
						Node tail = ComputerPlayer.getInstance().createNode(block.get(1));
						if (head.direction.equals(tail.direction))
							AnswerDeductionHelper.getTerrainStatus(ComputerPlayer.getInstance().getAllPlayerTrrianMap(),  areaTokenSet ,  player.getKey());
						else {
						while (head != tail) {
							AnswerDeductionHelper.getTerrainStatus(head.terrainList, areaTokenSet,  player.getKey());
							head = head.next;
							}
						}
						System.out.println("in for each " + areaTokenSet);
						if (Constants.BEACH_CHAR.equals(location.getKey())) {
							AnswerDeductionLogic.updateAreaTokenSet(location.getKey(),areaTokenSet);
							System.out.println("in for each " + areaTokenSet);
							
						}
						
						if(Constants.FOREST_CHAR.equals(location.getKey())) {
							AnswerDeductionLogic.updateAreaTokenSet(location.getKey(),areaTokenSet);
							System.out.println(areaTokenSet);
							System.out.println("in for each " + areaTokenSet);
							
						}
						
						if(Constants.MOUNTAINS_CHAR.equals(location.getKey())) {
							AnswerDeductionLogic.updateAreaTokenSet(location.getKey(),areaTokenSet);
							System.out.println(areaTokenSet);
							System.out.println("in for each " + areaTokenSet);
							
						}
						
						
						List<List<String>> value2 = AnswerDeductionLogic.updateStatus(areaTokenSet, location.getKey() , Integer.parseInt(block.get(2)), block.get(0), block.get(1), player.getKey() );
						System.out.println(value2);
						if(!Map.containsKey(location.getKey())) {
							Map.put(location.getKey(), value2);
							System.out.println("hello" + TentativeToken.get(player.getKey()).get(location.getKey()));
						}
						else {
							Map.remove(location.getKey());
							Map.put(location.getKey(), value2);
						}
					});

					if(!(TentativeToken.containsKey(player.getKey()))) {
						TentativeToken.put(player.getKey(), Map);
					}
				});
				
				System.out.println("Map " + TentativeToken);
				Map.clear();
			});
			
			
			ComputerPlayer.getInstance().setTentativeToken(TentativeToken);
			
		}
		
		
	}
	
	
	public static void checkAllToken(){
		
		System.out.println(ComputerPlayer.getInstance().getHead());
		Map<Integer , Set<String>> areaTokenSet = new HashMap<>();
		areaTokenSet.put(0, new HashSet<>());
		areaTokenSet.put(1, new HashSet<>());
		areaTokenSet.put(-1, new HashSet<>());
		HashSet<String> finalSet = new HashSet<>();
		ComputerPlayer.getInstance().setNorthSet(new HashSet<>());
		ComputerPlayer.getInstance().setNorthEastSet(new HashSet<>());
		ComputerPlayer.getInstance().setEastSet(new HashSet<>());
		ComputerPlayer.getInstance().setSouthEastSet(new HashSet<>());
		ComputerPlayer.getInstance().setSouthSet(new HashSet<>());
		ComputerPlayer.getInstance().setSouthWestSet(new HashSet<>());
		ComputerPlayer.getInstance().setWestSet(new HashSet<>());
		ComputerPlayer.getInstance().setNorthWestSet(new HashSet<>());





		Map<String, List<List<String>>> token = new HashMap<>();
		System.out.println("chackeall");
		
			for (String player : PlayerInformation.getInstance().getPlayerNameList()) {
				
				AnswerDeductionHelper.getTerrainStatus(ComputerPlayer.getInstance().getAllPlayerTrrianMap(),  areaTokenSet , player );
				areaTokenSet.entrySet().stream().forEach(tera->{
					tera.getValue().forEach(ter -> {
						
						if(tera.getKey() == -1) {
							ter.charAt(0);
							if(Character.toString(ter.charAt(0)).equals(Constants.NN) ) {
								
								if(!ComputerPlayer.getInstance().getNorthSet().contains(ter))
									ComputerPlayer.getInstance().getNorthSet().add(ter);
							}
							if(Character.toString(ter.charAt(0)).equals(Constants.NE) ) {
				
								if( !ComputerPlayer.getInstance().getNorthEastSet().contains(ter))
								ComputerPlayer.getInstance().getNorthEastSet().add(ter);
								}
							if(Character.toString(ter.charAt(0)).equals(Constants.EE)) {
								 if(!ComputerPlayer.getInstance().getEastSet().contains(ter))
									 ComputerPlayer.getInstance().getEastSet().add(ter);
							}
							if(Character.toString(ter.charAt(0)).equals(Constants.SE) ){
								
								if (!ComputerPlayer.getInstance().getSouthEastSet().contains(ter))
									ComputerPlayer.getInstance().getSouthEastSet().add(ter);
							}
							if(Character.toString(ter.charAt(0)).equals(Constants.SS) ) {
								
								
								if( !ComputerPlayer.getInstance().getSouthSet().contains(ter))
									ComputerPlayer.getInstance().getSouthSet().add(ter);
							}
							if(Character.toString(ter.charAt(0)).equals(Constants.SW) ) {
								
								
								if(!ComputerPlayer.getInstance().getSouthWestSet().contains(ter))
									ComputerPlayer.getInstance().getSouthWestSet().add(ter);
							}
							if(Character.toString(ter.charAt(0)).equals(Constants.WW)) {
								
								if (!ComputerPlayer.getInstance().getWestSet().contains(ter))
									ComputerPlayer.getInstance().getWestSet().add(ter);
							}
							if(Character.toString(ter.charAt(0)).equals(Constants.NW)) {
								
								if(!ComputerPlayer.getInstance().getNorthWestSet().contains(ter))
									ComputerPlayer.getInstance().getNorthWestSet().add(ter);
							}
							
						}
					});
				
					
				});
				
			}
			
			System.out.println("ComputerPlayer.getInstance().getNorthSet()" + ComputerPlayer.getInstance().getNorthSet());
			System.out.println(ComputerPlayer.getInstance().getEastSet());
			
			Map<String, Map<String, List<List<String>>>> tentativetoken = new HashMap<String, Map<String, List<List<String>>>>();
			if(ComputerPlayer.getInstance().getTentativeToken() != null) {
				ComputerPlayer.getInstance().getTentativeToken().entrySet().stream().forEach(play -> {
					tentativetoken.put(play.getKey(), new HashMap<>());
					play.getValue().entrySet().stream().forEach(terr -> {
						tentativetoken.get(play.getKey()).put(terr.getKey(), new ArrayList<>());
						List<List<String>> list = new ArrayList<>();
						List<String> list1 = new ArrayList<>();
						terr.getValue().forEach(loc->{
							tentativetoken.get(play.getKey()).get(terr.getKey()).add(new ArrayList<>());
							
							list1.add(loc.get(0));
							list1.add(loc.get(1));
							list1.add(loc.get(2));
							
						});
						list.add(list1);
						tentativetoken.get(play.getKey()).put(terr.getKey(), list);
						
						
					});
					
				});
			}
			System.out.println("ten " + tentativetoken);
			System.out.println("com "+ComputerPlayer.getInstance().getTentativeToken());
			
			if(tentativetoken != null) {
				tentativetoken.entrySet().stream().forEach(value -> {
				
				value.getValue().entrySet().stream().forEach(direc -> {
					direc.getValue().forEach(val -> {
						
						List<List<String>> y = new ArrayList<>();
						
						if(token.containsKey(direc.getKey())) {
							for (List<String> y1 : token.get(direc.getKey())) {
								
								if(y1.get(0).equals(val.get(0)) && y1.get(1).equals(val.get(1))){
									

									int count = Integer.parseInt(y1.get(2));
									int count2 = Integer.parseInt(val.get(2));
									int finalcount = count + count2;
									y1.set(2, Integer.toString(finalcount));
									
								}
								else {
									
									token.get(direc.getKey()).add(val);
								}
							
							}

						}
						
							else {
								
								token.put(direc.getKey(), direc.getValue());
							}

					});

				});
				
			});
		}
		System.out.println(ComputerPlayer.getInstance().getTentativeToken());

			
		token.entrySet().stream().forEach(to -> {
			
			to.getValue().forEach(value1 -> {
				
				Node head = ComputerPlayer.getInstance().createNode(value1.get(0));
				Node tail = ComputerPlayer.getInstance().createNode(value1.get(1));
				if (head.direction.equals(tail.direction))
					getLocation(value1.get(0) , to.getKey(), finalSet);
				else {
				while (head != tail) {
					
					System.out.println(head.direction);
					getLocation( head.direction, to.getKey(), finalSet);
					System.out.println("finalSet" + finalSet);
					head = head.next;
					
					}
				}
				
				if(Integer.toString(finalSet.size()).equals(value1.get(2))) {
					System.out.println("finalSet" + finalSet);
					updateTerrainTokenMap(finalSet,PlayerInformation.getInstance().getPlayerName());
					
				}
				
			});
			finalSet.clear();
		});
			
		System.out.println(token);
			
	}


	private static Set<String> getLocation(String location, String Terrain, HashSet<String> finalSet) {
		// TODO Auto-generated method stub
		
		
		if(location.equals(Constants.NORTH_CHAR))
			ComputerPlayer.getInstance().getNorthSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});
		if(location.equals(Constants.NORTH_EAST_CHAR))
			ComputerPlayer.getInstance().getNorthEastSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});		
		if(location.equals(Constants.EAST_CHAR))
			ComputerPlayer.getInstance().getEastSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});
		
		if(location.equals(Constants.SOUTH_EAST_CHAR))
			ComputerPlayer.getInstance().getSouthEastSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});
		if(location.equals(Constants.SOUTH_CHAR))
			ComputerPlayer.getInstance().getSouthSet().forEach(value ->{
				System.out.println("in SS" +Constants.SOUTH_CHAR);
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});
		if(location.equals(Constants.SOUTH_WEST_CHAR))
			ComputerPlayer.getInstance().getSouthWestSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});
		if(location.equals(Constants.WEST_CHAR))
			ComputerPlayer.getInstance().getWestSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});		
		if(location.equals(Constants.NORTH_WEST_CHAR))
			ComputerPlayer.getInstance().getNorthWestSet().forEach(value ->{
				if(Character.toString(value.charAt(1)).equals(Terrain) || Character.toString(value.charAt(1)).equals(Constants.ALL_CHAR));
					finalSet.add(value);
				
			});		
		return finalSet;
		
		
	}
	
}
