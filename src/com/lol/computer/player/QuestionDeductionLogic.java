package com.lol.computer.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;
import com.lol.validation.PlayerInfoValidation;

public class QuestionDeductionLogic {

	/**
	 * This method is used to introduce the question deduction logic in computer
	 * player
	 * 
	 * @param messageDetailsList
	 */
	public static void createQuestion(List<String> messageDetailsList) {

		SortedMap<Integer, String> messageMap = new TreeMap<Integer, String>();

		String dieFaceOne = messageDetailsList.get(0);
		String dieFaceTwo = messageDetailsList.get(1);
		String dieFaceThree = messageDetailsList.get(2);

		questionProxy(dieFaceOne, dieFaceTwo, messageMap);
		questionProxy(dieFaceOne, dieFaceThree, messageMap);
		questionProxy(dieFaceTwo, dieFaceOne, messageMap);
		questionProxy(dieFaceTwo, dieFaceThree, messageMap);
		questionProxy(dieFaceThree, dieFaceTwo, messageMap);
		questionProxy(dieFaceThree, dieFaceOne, messageMap);

		if (!messageMap.isEmpty()) {
			for (Entry<Integer, String> en : messageMap.entrySet()) {
				if (en.getKey() != 0) {
					String message = en.getValue();
					if (message != null && !message.isEmpty()) {
						System.out.println("Selected  Die Face  One :" + message.substring(3, 6));
						System.out.println("Selected  Die Face  Two :" + message.substring(7, 10));
						System.out.println("Selected  Area  Terrain  :" + message.substring(11, 12));
						System.out.println("Selected  Player :" + message.substring(13, 15));
						Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message);
						Utility.parseMessage(message);
						break;
					}
				}
			}
		} else {
			createQuestionRandomly(messageDetailsList);
		}
	}

	/**
	 * 
	 * @param dieFaceOne
	 * @param dieFaceTwo
	 * @param terrainType
	 * @param messageMap
	 * @return
	 */
	public static HashMap<String, HashMap<String, Integer>> generateQuestionMap(String dieFaceOne, String dieFaceTwo,
			String terrainType, SortedMap<Integer, String> messageMap) {
		Node current = ComputerPlayer.getInstance().getHead();
		HashMap<String, HashMap<String, Integer>> terrainCountMap = new HashMap<>();
		do {

			current.terrainList.entrySet().stream().forEach(terrianMap -> {

				terrianMap.getValue().entrySet().stream().forEach(playerMap -> {

					if (!playerMap.getKey().equals(PlayerInformation.getInstance().getPlayerName())
							&& playerMap.getValue() == -1) {

						if (!terrainCountMap.containsKey(playerMap.getKey()))
							terrainCountMap.put(playerMap.getKey(), new HashMap<String, Integer>());

						if (!terrainCountMap.get(playerMap.getKey()).containsKey(terrianMap.getKey().substring(1, 2)))
							terrainCountMap.get(playerMap.getKey()).put(terrianMap.getKey().substring(1, 2), 0);

						int count = terrainCountMap.get(playerMap.getKey()).get(terrianMap.getKey().substring(1, 2));
						count = count + 1;
						terrainCountMap.get(playerMap.getKey()).replace(terrianMap.getKey().substring(1, 2), count);
						
					}
				});
			});

			current = current.next;

		} while (current != ComputerPlayer.getInstance().getTail());

		HashSet<String> terrainTypes = new HashSet<String>();
		if (!Constants.ALL_CHAR.equals(terrainType)) {
			terrainTypes.add("M");
			terrainTypes.add("F");
			terrainTypes.add("B");
		} else {
			terrainTypes.add(terrainType);
		}

		terrainCountMap.entrySet().forEach(player -> {
			player.getValue().entrySet().stream().forEach(terrain -> {
				Integer terrainCount = 24;
				if (terrain.getValue() <= terrainCount && terrainTypes.contains(terrain.getKey())) {

					String message = "05:" + dieFaceOne + "," + dieFaceTwo + "," + terrainType + "," + player.getKey();

					messageMap.put(terrain.getValue(), message);
				}
			});
		});

		return terrainCountMap;

	}

	/**
	 * 
	 * @param dieFaceOne
	 * @param dieFaceTwo
	 * @param messageMap
	 */
	public static void questionProxy(String dieFaceOne, String dieFaceTwo, SortedMap<Integer, String> messageMap) {

		String directionOne = String.valueOf(dieFaceOne.charAt(0)) + String.valueOf(dieFaceOne.charAt(1));
		String directionTwo = String.valueOf(dieFaceTwo.charAt(0)) + String.valueOf(dieFaceTwo.charAt(1));
		String headTerrian = String.valueOf(dieFaceOne.charAt(2));
		String tailTerrian = String.valueOf(dieFaceTwo.charAt(2));

		ComputerPlayer.getInstance().setHead(ComputerPlayer.getInstance().createNode(directionOne));
		ComputerPlayer.getInstance().setTail(ComputerPlayer.getInstance().createNode(directionTwo));

		if (headTerrian.equals(tailTerrian) && !Constants.WILD_CHAR.equals(headTerrian)) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, tailTerrian, messageMap);
		} else if (!headTerrian.equals(tailTerrian)
				&& !(Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap);
		} else if (!headTerrian.equals(tailTerrian)
				&& (Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			String terrainType = !Constants.WILD_CHAR.equals(headTerrian) ? headTerrian : tailTerrian;
			generateQuestionMap(dieFaceOne, dieFaceTwo, terrainType, messageMap);
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap);
		}
	}

	/**
	 * This method is to build the question form human user input
	 * 05:NNF,NEF,A,P3
	 * 
	 * @param messageDetailsList
	 */

	private static void createQuestionRandomly(List<String> messageDetailsList) {
		System.out.println("Select Two Die Faces...... ");
		StringBuilder message = new StringBuilder(); // NN W,WW W,WW E
		message.append("05:");

		System.out.println("Enter DIE Face 1 :" + messageDetailsList);
		String dieFace1 = messageDetailsList.get(new Random().nextInt(messageDetailsList.size()));
		System.out.println("Selected DIE Face 1 :" + dieFace1);
		if (PlayerInfoValidation.getInstance().validateDiceFace(dieFace1) && messageDetailsList.contains(dieFace1)) {
			message.append(dieFace1).append(Constants.COMMA);
		} else {
			System.out.println("\n ====== Invalid DIE Face! Create question again ====== \n");
			createQuestion(messageDetailsList);
			return;
		}

		System.out.println("Enter DIE Face 2 :" + messageDetailsList);

		String dieFace2 = messageDetailsList.get(new Random().nextInt(messageDetailsList.size()));
		System.out.println("Selected DIE Face 2 :" + dieFace2);
		if (PlayerInfoValidation.getInstance().validateDiceFace(dieFace2) && messageDetailsList.contains(dieFace2)
				&& !dieFace2.equals(dieFace1)) {
			message.append(dieFace2).append(Constants.COMMA);
		} else {
			System.out.println(" ====== Invalid DIE Face! Create question again ====== \n");
			createQuestion(messageDetailsList);
			return;
		}

		String dieFace1Terrian = String.valueOf(dieFace1.charAt(2));
		String dieFace2Terrian = String.valueOf(dieFace2.charAt(2));

		if (Constants.FOREST_CHAR.equals(dieFace1Terrian) && Constants.FOREST_CHAR.equals(dieFace2Terrian)) {
			message.append(Constants.FOREST_CHAR).append(Constants.COMMA);
		} else if (Constants.BEACH_CHAR.equals(dieFace1Terrian) && Constants.BEACH_CHAR.equals(dieFace2Terrian)) {
			message.append(Constants.BEACH_CHAR).append(Constants.COMMA);
		} else if (Constants.MOUNTAINS_CHAR.equals(dieFace1Terrian)
				&& Constants.MOUNTAINS_CHAR.equals(dieFace2Terrian)) {
			message.append(Constants.MOUNTAINS_CHAR).append(Constants.COMMA);
		} else if (Constants.WILD_CHAR.equals(dieFace1Terrian) && Constants.WILD_CHAR.equals(dieFace2Terrian)) {
			System.out.println("Select Area Token: M-Mountains, F-Forest, B-Beach, A-all ");
			String[] areaTokenArr = { "A", "B", "F", "M" };
			String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];// "A";//sc.nextLine();
			System.out.println("Selected Area Terrian ::" + terrianToken);
			if (PlayerInfoValidation.getInstance().validateTerrianName(terrianToken))
				message.append(terrianToken).append(Constants.COMMA);
			else {
				System.out.println(" ====== Invalid Terrian Token! Ask Question Again!! ====== \n");
				createQuestion(messageDetailsList);
				return;
			}
		} else if (Constants.WILD_CHAR.equals(dieFace1Terrian) || Constants.WILD_CHAR.equals(dieFace2Terrian)) {

			if (Constants.FOREST_CHAR.equals(dieFace1Terrian) || Constants.FOREST_CHAR.equals(dieFace2Terrian)) {
				System.out.println("Select Area Token: A-all or " + Constants.FOREST_CHAR + "- Forest only");
				String[] areaTokenArr = { "A", "F" };
				String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];// "A";//sc.nextLine();
				System.out.println("Selected Area Terrian ::" + terrianToken);
				if (Constants.FOREST_CHAR.equals(terrianToken) || Constants.ALL_CHAR.equals(terrianToken))
					message.append(terrianToken).append(Constants.COMMA);
				else {
					System.out.println(" ====== Invalid Terrian Token! Ask Question Again!! ====== \n");
					createQuestion(messageDetailsList);
					return;
				}
			} else if (Constants.BEACH_CHAR.equals(dieFace1Terrian) || Constants.BEACH_CHAR.equals(dieFace2Terrian)) {
				System.out.println("Select Area Token: A-all or " + Constants.BEACH_CHAR + "- Beach only");
				String[] areaTokenArr = { "A", "B" };
				String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];// "A";//sc.nextLine();
				System.out.println("Selected Area Terrian ::" + terrianToken);
				if (Constants.BEACH_CHAR.equals(terrianToken) || Constants.ALL_CHAR.equals(terrianToken))
					message.append(terrianToken).append(Constants.COMMA);
				else {
					System.out.println(" ====== Invalid Terrian Token! Ask Question Again!! ====== \n");
					createQuestion(messageDetailsList);
					return;
				}

			} else if (Constants.MOUNTAINS_CHAR.equals(dieFace1Terrian)
					|| Constants.MOUNTAINS_CHAR.equals(dieFace2Terrian)) {
				System.out.println("Select Area Token: A-all or " + Constants.MOUNTAINS_CHAR + "- Mountain only");
				String[] areaTokenArr = { "A", "M" };
				String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];// "A";//sc.nextLine();
				System.out.println("Selected Area Terrian ::" + terrianToken);
				if (Constants.MOUNTAINS_CHAR.equals(terrianToken) || Constants.ALL_CHAR.equals(terrianToken))
					message.append(terrianToken).append(Constants.COMMA);
				else {
					System.out.println(" ====== Invalid Terrian Token! Ask Question Again!! ====== \n");
					createQuestion(messageDetailsList);
					return;
				}
			}

		} else {
			message.append(Constants.ALL_CHAR).append(Constants.COMMA);
		}

		System.out.println("Enter Player number whom you want to ask the question :");

		String playerAnswering = PlayerInformation.getInstance().getPlayerNameList()
				.get(new Random().nextInt(PlayerInformation.getInstance().getPlayerNameList().size()));
		System.out.println("Selected Player ::" + playerAnswering);
		if (PlayerInfoValidation.getInstance().validatePlayerName(playerAnswering)
				&& !playerAnswering.equalsIgnoreCase(PlayerInformation.getInstance().getPlayerName())) {
			message.append(playerAnswering);
		} else {
			System.out.println(" ====== Invalid Player! Create question again ====== \n");
			createQuestion(messageDetailsList);
			return;
		}

		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message.toString());
		Utility.parseMessage(message.toString());
	}

}
