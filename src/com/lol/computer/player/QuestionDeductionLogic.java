package com.lol.computer.player;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
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

/**
 * This class is used to deduce the question logic
 * 
 *
 */
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

	private static void pistol() {

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
		String message = Constants.MESSAAGE_05 + ":" + dieFaceOne + "," + dieFaceTwo + "," + tokenTerrain + "," + player
				+ "," + Constants.P_CHAR;
		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message);
		Utility.parseMessage(message);
	}

	/**
	 * This method is used to create the question intelligently
	 * 
	 * @param messageDetailsList
	 */
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
						if (message.substring(16).equals(Constants.S_CHAR)) {
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
	 * This method is used to generate question Map
	 * 
	 * @param dieFaceOne
	 * @param dieFaceTwo
	 * @param terrainType
	 * @param messageMap
	 * @param msgType
	 * @return
	 */
	private static HashMap<String, HashMap<String, Integer>> generateQuestionMap(String dieFaceOne, String dieFaceTwo,
			String terrainType, SortedMap<String, Integer> messageMap, SortedMap<String, Integer> messageMapAll,
			String msgType) {

		String directionOne = String.valueOf(dieFaceOne.charAt(0)) + String.valueOf(dieFaceOne.charAt(1));
		String directionTwo = String.valueOf(dieFaceTwo.charAt(0)) + String.valueOf(dieFaceTwo.charAt(1));
		ComputerPlayer.getInstance().setHead(ComputerPlayer.getInstance().createNode(directionOne));
		ComputerPlayer.getInstance().setTail(ComputerPlayer.getInstance().createNode(directionTwo));
		Node current = ComputerPlayer.getInstance().getHead();
		HashMap<String, HashMap<String, Integer>> terrainCountMap = new HashMap<>();
		// counting the location between two directions
		do {

			current.terrainList.entrySet().stream().forEach(terrianMap -> {

				terrianMap.getValue().entrySet().stream().forEach(playerMap -> {

					if (playerMap.getValue() == Constants.TENTATIVE_TERRAIN) {
						String terrainChar = terrianMap.getKey().substring(1, 2);
						if (!terrainCountMap.containsKey(playerMap.getKey()))
							terrainCountMap.put(playerMap.getKey(), new HashMap<String, Integer>());

						if (!terrainCountMap.get(playerMap.getKey()).containsKey(terrainChar))
							terrainCountMap.get(playerMap.getKey()).put(terrainChar, 0);

						int count = terrainCountMap.get(playerMap.getKey()).get(terrainChar);

						terrainCountMap.get(playerMap.getKey()).replace(terrainChar, ++count);

					}
				});
			});

			current = current.next;

		} while (current != ComputerPlayer.getInstance().getTail());

		HashSet<String> terrainTypes = new HashSet<String>();
		if (Constants.ALL_CHAR.equals(terrainType)) {
			terrainTypes.add(Constants.MOUNTAINS_CHAR);
			terrainTypes.add(Constants.FOREST_CHAR);
			terrainTypes.add(Constants.BEACH_CHAR);
		} else {
			terrainTypes.add(terrainType);
		}

		// creating valid question messages
		terrainCountMap.entrySet().forEach(player -> {
			player.getValue().entrySet().stream().forEach(terrain -> {
				Integer terrainCount = 24;
				if (terrain.getValue() <= terrainCount && terrainTypes.contains(terrain.getKey())) {

					String message = Constants.MESSAAGE_05 + ":" + dieFaceOne + "," + dieFaceTwo + "," + terrainType
							+ "," + player.getKey();

					if (msgType.equals(Constants.S_CHAR)) {
						message = message.concat("," + Constants.S_CHAR);
						ComputerPlayer.getInstance().setShovelFlag(); // shovel used marked
					} else if (msgType.equals(Constants.Q_CHAR)) {
						message = message.concat("," + Constants.Q_CHAR); // default message type
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
	 * Creating question based on the direction
	 * 
	 * @param dieFaceOne
	 * @param dieFaceTwo
	 * @param messageMap
	 */
	private static void questionProxy(String dieFaceOne, String dieFaceTwo, SortedMap<String, Integer> messageMap,
			SortedMap<String, Integer> messageMapAll) {

		String headTerrian = String.valueOf(dieFaceOne.charAt(2));
		String tailTerrian = String.valueOf(dieFaceTwo.charAt(2));

		HashSet<String> terrains = new HashSet<String>(
				Arrays.asList(Constants.MOUNTAINS_CHAR, Constants.BEACH_CHAR, Constants.FOREST_CHAR));

		if (headTerrian.equals(tailTerrian) && !Constants.WILD_CHAR.equals(headTerrian)) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, tailTerrian, messageMap, messageMapAll, Constants.Q_CHAR);
			if (SetShovel && ComputerPlayer.getInstance().getRoundCount() > 10) {
				terrains.remove(dieFaceOne.substring(2, 3));
				dieFaceOne = dieFaceOne.substring(0, 2)
						+ terrains.stream().skip(new Random().nextInt(terrains.size())).findFirst().orElse(null);
				generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap, messageMapAll,
						Constants.S_CHAR);
			}

		} else if (!headTerrian.equals(tailTerrian)
				&& !(Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap, messageMapAll,
					Constants.Q_CHAR);
			if (SetShovel && ComputerPlayer.getInstance().getRoundCount() > 10) {
				String temp = dieFaceTwo.substring(2, 3);
				terrains.remove(temp);
				String dieFaceTwo2 = dieFaceTwo.subSequence(0, 2) + headTerrian;
				generateQuestionMap(dieFaceOne, dieFaceTwo2, headTerrian, messageMap, messageMapAll, Constants.S_CHAR);
				terrains.add(temp);
				temp = dieFaceOne.substring(2, 3);
				terrains.remove(temp);
				String dieFaceOne1 = dieFaceOne.substring(0, 2) + tailTerrian;
				generateQuestionMap(dieFaceOne1, dieFaceTwo, tailTerrian, messageMap, messageMapAll, Constants.S_CHAR);

			}

		} else if (!headTerrian.equals(tailTerrian)
				&& (Constants.WILD_CHAR.equals(headTerrian) || Constants.WILD_CHAR.equals(tailTerrian))) {
			String terrainType = !Constants.WILD_CHAR.equals(headTerrian) ? headTerrian : tailTerrian;
			generateQuestionMap(dieFaceOne, dieFaceTwo, terrainType, messageMap, messageMapAll, Constants.Q_CHAR);
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.ALL_CHAR, messageMap, messageMapAll,
					Constants.Q_CHAR);
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
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.MOUNTAINS_CHAR, messageMap, messageMapAll,
					Constants.Q_CHAR);
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.BEACH_CHAR, messageMap, messageMapAll,
					Constants.Q_CHAR);
			generateQuestionMap(dieFaceOne, dieFaceTwo, Constants.FOREST_CHAR, messageMap, messageMapAll,
					Constants.Q_CHAR);
		}
	}

	/**
	 * Create question randomly in case any exception
	 * 
	 * @param messageDetailsList
	 */
	private static void createQuestionRandomly(List<String> messageDetailsList) {

		StringBuilder message = new StringBuilder(); // NN W,WW W,WW E
		message.append(Constants.MESSAAGE_05).append(":");
		String dieFace1 = messageDetailsList.get(new Random().nextInt(messageDetailsList.size()));
		if (PlayerInfoValidation.getInstance().validateDiceFace(dieFace1) && messageDetailsList.contains(dieFace1)) {
			message.append(dieFace1).append(Constants.COMMA);
		} else {
			createQuestionRandomly(messageDetailsList);
			return;
		}
		String dieFace2 = messageDetailsList.get(new Random().nextInt(messageDetailsList.size()));
		if (PlayerInfoValidation.getInstance().validateDiceFace(dieFace2) && messageDetailsList.contains(dieFace2)
				&& !dieFace2.equals(dieFace1)) {
			message.append(dieFace2).append(Constants.COMMA);
		} else {
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

			String[] areaTokenArr = { "A", "B", "F", "M" };
			String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];

			if (PlayerInfoValidation.getInstance().validateTerrianName(terrianToken))
				message.append(terrianToken).append(Constants.COMMA);
			else {
				createQuestionRandomly(messageDetailsList);
				return;
			}
		} else if (Constants.WILD_CHAR.equals(dieFace1Terrian) || Constants.WILD_CHAR.equals(dieFace2Terrian)) {

			if (Constants.FOREST_CHAR.equals(dieFace1Terrian) || Constants.FOREST_CHAR.equals(dieFace2Terrian)) {

				String[] areaTokenArr = { "A", "F" };
				String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];
				if (Constants.FOREST_CHAR.equals(terrianToken) || Constants.ALL_CHAR.equals(terrianToken))
					message.append(terrianToken).append(Constants.COMMA);
				else {
					createQuestionRandomly(messageDetailsList);
					return;
				}
			} else if (Constants.BEACH_CHAR.equals(dieFace1Terrian) || Constants.BEACH_CHAR.equals(dieFace2Terrian)) {

				String[] areaTokenArr = { "A", "B" };
				String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];
				if (Constants.BEACH_CHAR.equals(terrianToken) || Constants.ALL_CHAR.equals(terrianToken))
					message.append(terrianToken).append(Constants.COMMA);
				else {
					createQuestionRandomly(messageDetailsList);
					return;
				}

			} else if (Constants.MOUNTAINS_CHAR.equals(dieFace1Terrian)
					|| Constants.MOUNTAINS_CHAR.equals(dieFace2Terrian)) {
				String[] areaTokenArr = { "A", "M" };
				String terrianToken = areaTokenArr[new Random().nextInt(areaTokenArr.length)];

				if (Constants.MOUNTAINS_CHAR.equals(terrianToken) || Constants.ALL_CHAR.equals(terrianToken))
					message.append(terrianToken).append(Constants.COMMA);
				else {
					createQuestionRandomly(messageDetailsList);
					return;
				}
			}

		} else {
			message.append(Constants.ALL_CHAR).append(Constants.COMMA);
		}

		String playerAnswering = PlayerInformation.getInstance().getPlayerNameList()
				.get(new Random().nextInt(PlayerInformation.getInstance().getPlayerNameList().size()));

		if (PlayerInfoValidation.getInstance().validatePlayerName(playerAnswering)
				&& !playerAnswering.equalsIgnoreCase(PlayerInformation.getInstance().getPlayerName())) {
			message.append(playerAnswering);
		} else {
			createQuestionRandomly(messageDetailsList);
			return;
		}
		message.append("," + Constants.Q_CHAR);
		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message.toString());
		Utility.parseMessage(message.toString());
	}

	/**
	 * This is used to create the Barrel message
	 * 
	 * @param numberofDies
	 * @param die1
	 * @param die2
	 * @param messageDetailsList
	 * @return
	 */
	private static String RerollDieReq(int numberofDies, String die1, String die2, List<String> messageDetailsList) {
		return Constants.MESSAAGE_12 + ":" + numberofDies + "," + messageDetailsList.indexOf(die1) + ","
				+ messageDetailsList.indexOf(die2);

	}
}
