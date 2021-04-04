package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class AnswerDeductionLogic {
	
	static Boolean flag = false;

	public static void getAnswerInformation(String messageNumber, List<String> messageDetailsList) {
		System.out.println("first " + ComputerPlayer.getInstance().getAllPlayerTrrianMap());

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
		String locFound = checkIsTreasureLocFound();
		System.out.println("Message [" + messageNumber + "] Player " + messageDetailsList.get(4) + " has "
				+ messageDetailsList.get(3) + " "
				+ PlayerInformation.getInstance().getTerrianTokenInformation(messageDetailsList.get(2))
				+ " terrain between "
				+ PlayerInformation.getInstance().getDirectionInformation(messageDetailsList.get(0)) + " and "
				+ PlayerInformation.getInstance().getDirectionInformation(messageDetailsList.get(1)) + "\n");
		if (!PlayerInformation.getInstance().getPlayerName().equals(messageDetailsList.get(4)) && !Constants.YES.equals(locFound)) {
			processAnswerMessage(messageDetailsList);
		
//		AnswerDeductionHelper.checkAllToken();
		}
		System.out.println(ComputerPlayer.getInstance().getNotTreasureLoc());
		System.out.println("final" + ComputerPlayer.getInstance().getAllPlayerTrrianMap());
	}

	
	
	public static void processAnswerMessage(List<String> messageDetailsList) {

		String playerName = messageDetailsList.get(4);
		
		String Diretion1 = messageDetailsList.get(0).substring(0, 2);
		String Diretion2 = messageDetailsList.get(1).substring(0, 2);
		
		Map<Integer , Set<String>> areaTokenSet = new HashMap<>();
		areaTokenSet.put(0, new HashSet<>());
		areaTokenSet.put(1, new HashSet<>());
		areaTokenSet.put(-1, new HashSet<>());
	
		
		Node head = ComputerPlayer.getInstance().createNode(Diretion1);
		Node tail = ComputerPlayer.getInstance().createNode(Diretion2);
		
		if (head.direction.equals(tail.direction))
				AnswerDeductionHelper.getTerrainStatus(ComputerPlayer.getInstance().getAllPlayerTrrianMap(),  areaTokenSet ,  playerName);
		else {
			while (head != tail) {
				AnswerDeductionHelper.getTerrainStatus(head.terrainList, areaTokenSet,  playerName);
				head = head.next;
			}
		}
//		System.out.println("before all beach" + areaTokenSet);

		CheckTerrainStatus(areaTokenSet,messageDetailsList);
		

	}
	
	private static void CheckTerrainStatus(Map<Integer , Set<String>> areaTokenSet, List<String> messageDetailsList) {
		
		
		String areaToken = messageDetailsList.get(2);
		String noOfTokens = messageDetailsList.get(3);
		int tokenCount = Integer.valueOf(noOfTokens);
		String Diretion1 = messageDetailsList.get(0).substring(0, 2);
		String Diretion2 = messageDetailsList.get(1).substring(0, 2);
		String playerName = messageDetailsList.get(4);
			
		if (Constants.BEACH_CHAR.equals(areaToken)) {
			updateAreaTokenSet(areaToken,areaTokenSet);
			
		}
		
		if(Constants.FOREST_CHAR.equals(areaToken)) {
			updateAreaTokenSet(areaToken,areaTokenSet);
			
		}
		
		if(Constants.MOUNTAINS_CHAR.equals(areaToken)) {
			updateAreaTokenSet(areaToken,areaTokenSet);
			
		}
		
		if(Constants.ALL_CHAR.equals(areaToken)) {
			
			
		}
		
//		System.out.println("Area token set" + areaTokenSet);
		List<List<String>> value = updateStatus(areaTokenSet,areaToken,tokenCount,Diretion1,Diretion2,playerName);
		if(ComputerPlayer.getInstance().getTentativeToken() != null) {
		
		if(ComputerPlayer.getInstance().getTentativeToken().get(playerName).get(areaToken)!=null && !(value.isEmpty())) {
			ComputerPlayer.getInstance().getTentativeToken().get(playerName).remove(areaToken);
			ComputerPlayer.getInstance().getTentativeToken().get(playerName).put(areaToken, value);
		}
		
		}
		
//		System.out.println("ComputerPlayer.getInstance().getTentativeToken() out " + ComputerPlayer.getInstance().getTentativeToken());

		
	}
	

	
	public static List<List<String>> updateStatus(Map<Integer, Set<String>> areaTokenSet, String areaToken, int tokenCount , String Diretion1 , String Diretion2, String playerName) {
		// TODO Auto-generated method stub
		List<List<String>> value2 = new ArrayList<>();
		int tokenCountTemp = 0;
		
//		System.out.println("update status");
		if (tokenCount == 0 && !areaTokenSet.get(-1).isEmpty()) {
//			System.out.println("hete");
			updateZeroterrainTokenInformation(areaTokenSet.get(-1), playerName);
			
		}

		else if(areaTokenSet.get(1).size() > 0 ) {
//			System.out.println("hete2");

			tokenCountTemp = tokenCount - areaTokenSet.get(1).size();
		}
		
		if (tokenCountTemp > 0 && !(areaTokenSet.get(-1).isEmpty())) {
//			System.out.println("het2");

			if (areaTokenSet.get(-1).size() == tokenCountTemp) {
				
				AnswerDeductionHelper.updateTerrainTokenMap(areaTokenSet.get(-1), playerName);
			
			}
			if(areaTokenSet.get(-1).size() > tokenCountTemp) {
				
				List<String> value = new ArrayList<>();
				value.add(Diretion1);
				value.add(Diretion2);
				value.add(Integer.toString(tokenCount));
					if(ComputerPlayer.getInstance().getTentativeToken() == null) {
						Map<String, Map<String, List<List<String>>>> tentativeToken = new HashMap<>();
						List<String> PlayerList1 = PlayerInformation.getInstance().getPlayerNameList();
						for (String player : PlayerList1) {
							if(!player.equals(PlayerInformation.getInstance().getPlayerName()))
								tentativeToken.put(player, new HashMap<>());
							
						}
						ComputerPlayer.getInstance().setTentativeToken(tentativeToken);
						
					}
					if (ComputerPlayer.getInstance().getTentativeToken().get(playerName).get(areaToken) == null) {
							List<List<String>> contain = new ArrayList<>();
							contain.add(value);
							ComputerPlayer.getInstance().getTentativeToken().get(playerName).put(areaToken, contain);
									
									
					}
					else {
						ComputerPlayer.getInstance().getTentativeToken().get(playerName).get(areaToken).forEach( loca ->{
							
//							System.out.println("loca"  +loca );
							value2.add(loca);
							if(!(loca.get(0).equals(Diretion1)) && !(loca.get(1).equals(Diretion2))) {
									flag = true;
//								System.out.println("in true flag");
								System.out.println(value2);
							}
						});
						if(flag.equals(true)) {
							value2.add(value);
							flag = false;
						}
					}
				
			}
//			System.out.println("ComputerPlayer.getInstance().getTentativeToken() in " + ComputerPlayer.getInstance().getTentativeToken());
		}
		
		
		return value2;
	}



	public static Map<Integer, Set<String>> updateAreaTokenSet(String areaToken, Map<Integer, Set<String>> areaTokenSet) {

		areaTokenSet.entrySet().stream().forEach(value ->{
					 
				 value.getValue().removeIf((terrain) -> (!Character.toString(terrain.charAt(1)).equals(areaToken)));
			
		});
		
		return areaTokenSet;
	}



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

	private static void updateZeroterrainTokenInformation(Set<String> terrainToken, String playerName ) {
		List<String> PlayerList = PlayerInformation.getInstance().getPlayerNameList();
		for (String player1 : PlayerList) {
			// marking 1 for having the token

			for (String terrain : terrainToken) {
				Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap()
						.get(terrain);
				if (terrainMap != null) {
					if (playerName.equals(player1))
						terrainMap.put(player1, 0);
					else
						terrainMap.put(player1, ComputerPlayer.getInstance().getAllPlayerTrrianMap()
								.get(terrain).get(player1));

					ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrain, terrainMap);
				}
			}
		}
		
		AnswerDeductionHelper.checkTentativeTerrain();

	}
	
	public static void updateterrainMapForNotTreasureLoc(List<String> updatedterrainList, String playerName) {
		for (String terrainToken : updatedterrainList) {
			Map<String, Integer> terrainMap = ComputerPlayer.getInstance().getAllPlayerTrrianMap().get(terrainToken);
			terrainMap.put(playerName, 0);
			ComputerPlayer.getInstance().getAllPlayerTrrianMap().put(terrainToken, terrainMap);
		}
	}
}