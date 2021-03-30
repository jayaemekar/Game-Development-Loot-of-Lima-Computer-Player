package com.lol.computer.player;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;

public class QuestionDeductionLogic {

	private static int terrainCount = 24;
	private static String Message = "";


	/**
	 * 
	 * @param messageDetailsList
	 * @return
	 */
	public static String createQuestion(List<String> messageDetailsList) {

		HashMap<String, Integer> list = new HashMap<String, Integer>();

		String dieFaceOne = messageDetailsList.get(0);
		String dieFaceTwo = messageDetailsList.get(1);
		String dieFaceThree = messageDetailsList.get(2);

		questionProxy(dieFaceOne, dieFaceTwo, list);
		questionProxy(dieFaceOne, dieFaceThree, list);
		questionProxy(dieFaceTwo, dieFaceOne, list);
		questionProxy(dieFaceTwo, dieFaceThree, list);
		questionProxy(dieFaceThree, dieFaceTwo, list);
		questionProxy(dieFaceThree, dieFaceOne, list);
		sortByValue(list);

		for (Map.Entry<String, Integer> en : list.entrySet()) {
			if (en.getValue() != 0) {
				Message = en.getKey();

				System.out.println("Selected  Die Face  One :" + Message.substring(3,6));
				System.out.println("Selected  Die Face  Two :" + Message.substring(7,10));
				System.out.println("Selected  Area  Terrain  :" + Message.substring(11,12));
				System.out.println("Selected  Player :" + Message.substring(13,15));
				break;
			} else {
				Message = en.getKey();
			}
		}

		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), Message);
		Utility.parseMessage(Message);
		return Message;
	}

	/**
	 * 
	 * @param dieFaceOne
	 * @param dieFaceTwo
	 * @param terrainType
	 * @param list
	 * @return
	 */
	public static HashMap<String, HashMap<String, Integer>> generateQuestionMap(String dieFaceOne, String dieFaceTwo,
			String terrainType, HashMap<String, Integer> list) {
		Node current = ComputerPlayer.getInstance().getHead();
		HashMap<String, HashMap<String, Integer>> terrainCountMap = new HashMap<>();
		//P1: B:2,M:3,F;4
		do {

			current.terrainList.entrySet().stream().forEach(terrianMap -> {
				//B1: {P1:1,P2:0,P3:-1},F1,M1
				terrianMap.getValue().entrySet().stream().forEach(playerMap -> {
					//P1:1,P2:0,P3:-1
					if (!playerMap.getKey().equals(PlayerInformation.getInstance().getPlayerName())
							&& playerMap.getValue() == -1) {

						if (!terrainCountMap.containsKey(playerMap.getKey()))
							terrainCountMap.put(playerMap.getKey(), new HashMap<String, Integer>());

						if (!terrainCountMap.get(playerMap.getKey()).containsKey(terrianMap.getKey().substring(1, 2)))
							terrainCountMap.get(playerMap.getKey()).put(terrianMap.getKey().substring(1, 2), 0);

						int count = 0;
						count = terrainCountMap.get(playerMap.getKey()).get(terrianMap.getKey().substring(1, 2));
						count = count + 1;

						terrainCountMap.get(playerMap.getKey()).replace(terrianMap.getKey().substring(1, 2), count);
					}
				});
			});

			current = current.next;

		} while (current != ComputerPlayer.getInstance().getTail());

		HashSet<String> terrainTypes = new HashSet<String>();
		if (Constants.ALL_CHAR.equals(terrainType)) {
			terrainTypes.add("M");
			terrainTypes.add("F");
			terrainTypes.add("B");
		} else {
			terrainTypes.add(terrainType);
		}

		terrainCountMap.entrySet().forEach(player -> {
			player.getValue().entrySet().stream().forEach(terrain -> {
				if (terrain.getValue() <= terrainCount && terrainTypes.contains(terrain.getKey())) {

					Message = "05:" + dieFaceOne + "," + dieFaceTwo + "," + terrainType + "," + player.getKey();;
					terrainCount = terrain.getValue();
				}
			});
		});
		list.put(Message, terrainCount);
		return terrainCountMap;

	}

	/**
	 * 
	 * @param dieFaceOne
	 * @param dieFaceTwo
	 * @param list
	 */
	public static void questionProxy(String dieFaceOne, String dieFaceTwo, HashMap<String, Integer> list) {

		String directionOne = String.valueOf(dieFaceOne.charAt(0)) + String.valueOf(dieFaceOne.charAt(1));
		String directionTwo = String.valueOf(dieFaceTwo.charAt(0)) + String.valueOf(dieFaceTwo.charAt(1));
		String headTerrian = String.valueOf(dieFaceOne.charAt(2));
		String tailTerrian = String.valueOf(dieFaceTwo.charAt(2));

		ComputerPlayer.getInstance().setHead(ComputerPlayer.getInstance().createNode(directionOne));
		ComputerPlayer.getInstance().setTail(ComputerPlayer.getInstance().createNode(directionTwo));

		if (headTerrian.equals(tailTerrian) && !Constants.WILD_CHAR.equals(headTerrian)) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, tailTerrian, list);
		} else if (!headTerrian.equals(tailTerrian)
				&& !(Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, list);
		} else if (!headTerrian.equals(tailTerrian)
				&& (Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			String terrainType = !Constants.WILD_CHAR.equals(headTerrian) ? headTerrian : tailTerrian;
			generateQuestionMap(dieFaceOne, dieFaceTwo, terrainType, list);
			generateQuestionMap(dieFaceOne, dieFaceTwo,Constants.ALL_CHAR, list);
		}
	}

	/**
	 * 
	 * @param hm
	 * @return
	 */
	private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

}
