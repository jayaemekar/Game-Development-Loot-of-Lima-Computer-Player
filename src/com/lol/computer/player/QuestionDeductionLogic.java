package com.lol.computer.player;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;
import com.lol.validation.PlayerInfoValidation;

public class QuestionDeductionLogic {

	private static Boolean SetShovel = true;
	private static Boolean SetPistol = false;
	private static Boolean Barrel = false;

	/**
	 * This method is used to introduce the question deduction logic in computer
	 * player
	 * 
	 * @param messageDetailsList
	 */

	public static void pistol() {

		HashSet<String> defaultMap = new HashSet<String>(ComputerPlayer.getInstance().getAllPlayerTrrianMap().keySet());
		HashSet<String> nonTreasureLocation = new HashSet<String>(ComputerPlayer.getInstance().getNotTreasureLoc());
		HashSet<String> players = new HashSet<String>(ComputerPlayer.getInstance().getPlayerObj().keySet());
		defaultMap.removeAll(nonTreasureLocation);

		int n = new Random().nextInt(defaultMap.size());

		String token = defaultMap.stream().skip(n).findFirst().orElse(null);
		String[] directions = { "NN", "NW", "WW", "SW", "SS", "SE", "EE", "NE" };
		int tokenNumber = Integer.parseInt(token.substring(0, 1)) - 1;
		String tokenTerrain = token.substring(1, 2);

		String firstToken = directions[tokenNumber];
		String secondToken = "";
		if (tokenNumber + 1 > 7) {
			secondToken = "NN";
		} else {
			secondToken = directions[tokenNumber + 1];
		}
		String dieFaceOne = firstToken + tokenTerrain;
		String dieFaceTwo = secondToken + tokenTerrain;
		String player = players.stream().skip(new Random().nextInt(players.size())).findFirst().orElse(null);
		String message = "05:" + dieFaceOne + "," + dieFaceTwo + "," + tokenTerrain + "," + player + ",P";
		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message);
		Utility.parseMessage(message);
	}

	public static void createQuestion(List<String> messageDetailsList) {

		if (ComputerPlayer.getInstance().getNotTreasureLoc().size() >= 20 && !SetPistol) {
			pistol();
			SetPistol = true;
			return;
		}

		SortedMap<String, Integer> messageMap = new TreeMap<>();

		String dieFaceOne = messageDetailsList.get(0);
		String dieFaceTwo = messageDetailsList.get(1);
		String dieFaceThree = messageDetailsList.get(2);

		SortedMap<String, Integer> messageMapAll = new TreeMap<>();
		questionProxy(dieFaceOne, dieFaceTwo, messageMap, messageMapAll);
		questionProxy(dieFaceOne, dieFaceThree, messageMap, messageMapAll);
		questionProxy(dieFaceTwo, dieFaceOne, messageMap, messageMapAll);
		questionProxy(dieFaceTwo, dieFaceThree, messageMap, messageMapAll);
		questionProxy(dieFaceThree, dieFaceTwo, messageMap, messageMapAll);
		questionProxy(dieFaceThree, dieFaceOne, messageMap, messageMapAll);

		if (messageMap.isEmpty())
			messageMap = messageMapAll;
		Map<String, Integer> sortedMapByValue = messageMap.entrySet().stream().sorted(comparingByValue())
				.collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
		System.out.println("sortedMapByValue  :: " + sortedMapByValue);

		if (!sortedMapByValue.isEmpty()) {
			for (Entry<String, Integer> en : sortedMapByValue.entrySet()) {
				if (en.getValue() != 0) {
					String message = en.getKey();

					if (message != null && !message.isEmpty() && Barrel == false) {
						System.out.println("Selected  Die Face  One :" + message.substring(3, 6));
						System.out.println("Selected  Die Face  Two :" + message.substring(7, 10));
						System.out.println("Selected  Area  Terrain  :" + message.substring(11, 12));
						System.out.println("Selected  Player :" + message.substring(13, 15));

						System.out.println("Message Type : " + message.substring(16));
						if (message.substring(16).equals("S")) {
							SetShovel = false;
							ComputerPlayer.getInstance().setShovelFlag();
						}
						Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message);
						Utility.parseMessage(message);
						break;
					} else if (en.getValue() > 3 && ComputerPlayer.getInstance().getBarrelFlagStatus() == false) {

						Barrel = true;
						String RerollMessage = RerollDieReq(2, message.substring(3, 6), message.substring(7, 10),
								messageDetailsList);
						Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), RerollMessage);
						Utility.parseMessage(RerollMessage);
						ComputerPlayer.getInstance().setBarrelFlag();
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
	 * @param msgType
	 * @return
	 */
	public static HashMap<String, HashMap<String, Integer>> generateQuestionMap(String dieFaceOne, String dieFaceTwo,
			String terrainType, SortedMap<String, Integer> messageMap, SortedMap<String, Integer> messageMapAll,
			String msgType) {

		System.out.println("messageMap####:" + messageMap);
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
		if (Constants.ALL_CHAR.equals(terrainType)) {
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

					if (msgType.equals("S")) {
						message = message.concat(",S");
						ComputerPlayer.getInstance().setShovelFlag(); // shovel used marked
					} else if (msgType.equals("Q")) {
						message = message.concat(",Q"); // default message type
					}
					if (!dieFaceOne.substring(2).equals(dieFaceTwo.substring(2)))
						if (!Constants.ALL_CHAR.equals(terrainType))
							messageMap.put(message, terrain.getValue());
						else
							messageMapAll.put(message, terrain.getValue());
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
	public static void questionProxy(String dieFaceOne, String dieFaceTwo, SortedMap<String, Integer> messageMap,
			SortedMap<String, Integer> messageMapAll) {

		String directionOne = String.valueOf(dieFaceOne.charAt(0)) + String.valueOf(dieFaceOne.charAt(1));
		String directionTwo = String.valueOf(dieFaceTwo.charAt(0)) + String.valueOf(dieFaceTwo.charAt(1));
		String headTerrian = String.valueOf(dieFaceOne.charAt(2));
		String tailTerrian = String.valueOf(dieFaceTwo.charAt(2));

		ComputerPlayer.getInstance().setHead(ComputerPlayer.getInstance().createNode(directionOne));
		ComputerPlayer.getInstance().setTail(ComputerPlayer.getInstance().createNode(directionTwo));

		HashSet<String> terrains = new HashSet<String>();
		terrains.add("M");
		terrains.add("B");
		terrains.add("F");

		if (headTerrian.equals(tailTerrian) && !Constants.WILD_CHAR.equals(headTerrian)) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, tailTerrian, messageMap, messageMapAll, "Q");
			if (SetShovel && ComputerPlayer.getInstance().getRoundCount() > 10) {
				terrains.remove(dieFaceOne.substring(2, 3));
				dieFaceOne = dieFaceOne.substring(0, 2)
						+ terrains.stream().skip(new Random().nextInt(terrains.size())).findFirst().orElse(null);
				generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap, messageMapAll, "S");
			}

		} else if (!headTerrian.equals(tailTerrian)
				&& !(Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap, messageMapAll, "Q");
			if (SetShovel && ComputerPlayer.getInstance().getRoundCount() > 10) {
				String temp = dieFaceTwo.substring(2, 3);
				terrains.remove(temp);
				String dieFaceTwo2 = dieFaceTwo.subSequence(0, 2) + headTerrian;
				generateQuestionMap(dieFaceOne, dieFaceTwo2, headTerrian, messageMap, messageMapAll, "S");
				terrains.add(temp);
				temp = dieFaceOne.substring(2, 3);
				terrains.remove(temp);
				String dieFaceOne1 = dieFaceOne.substring(0, 2) + tailTerrian;
				generateQuestionMap(dieFaceOne1, dieFaceTwo, tailTerrian, messageMap, messageMapAll, "S");

			}

		} else if (!headTerrian.equals(tailTerrian)
				&& (Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			String terrainType = !Constants.WILD_CHAR.equals(headTerrian) ? headTerrian : tailTerrian;
			generateQuestionMap(dieFaceOne, dieFaceTwo, terrainType, messageMap, messageMapAll, "Q");
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap, messageMapAll, "Q");
			if (SetShovel && ComputerPlayer.getInstance().getRoundCount() > 10) {
				terrains.remove(terrainType);
				Iterator<String> terrainIterator = terrains.iterator();
				while (terrainIterator.hasNext()) {
					String dieFaceOne1 = dieFaceOne;
					String dieFaceTwo2 = dieFaceTwo;
					String ty = (String) terrainIterator.next();
					if (!headTerrian.equals(Constants.WILD_CHAR)) {
						dieFaceOne1 = dieFaceOne1.substring(0, 2) + ty;
					} else {
						dieFaceTwo2 = dieFaceTwo2.substring(0, 2) + ty;
					}
					generateQuestionMap(dieFaceOne1, dieFaceTwo2, ty, messageMap, messageMapAll, "Q");
				}
			}
		} else {
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.MOUNTAINS_CHAR, messageMap, messageMapAll, "Q");
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.BEACH_CHAR, messageMap, messageMapAll, "Q");
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.FOREST_CHAR, messageMap, messageMapAll, "Q");
		}
	}

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
			createQuestionRandomly(messageDetailsList);
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
			createQuestionRandomly(messageDetailsList);
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
				createQuestionRandomly(messageDetailsList);
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
					createQuestionRandomly(messageDetailsList);
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
					createQuestionRandomly(messageDetailsList);
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
					createQuestionRandomly(messageDetailsList);
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
			createQuestionRandomly(messageDetailsList);
			return;
		}
		message.append(",Q");
		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message.toString());
		Utility.parseMessage(message.toString());
	}

	public static String RerollDieReq(int numberofDies, String die1, String die2, List<String> messageDetailsList) {
		return Constants.MESSAAGE_12 + numberofDies + "," + messageDetailsList.indexOf(die1) + ","
				+ messageDetailsList.indexOf(die2);

	}
}
