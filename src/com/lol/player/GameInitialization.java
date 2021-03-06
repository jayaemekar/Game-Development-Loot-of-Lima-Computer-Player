package com.lol.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.computer.player.ComputerPlayerInitialization;
import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;
import com.lol.validation.PlayerInfoValidation;

public class GameInitialization {

	private boolean isLeftOverToken = false;
	private boolean isSwapPlayerToken = false;
	Map<String, List<String>> personalTokenMap = new HashMap<>();

	/**
	 * Notify the game is ready to play and determine the number of players
	 * 01:<Number of Players> e.g.01:03
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */

	public void gameReadyInformation(String messageNumber, List<String> messageDetailsList) {

		PlayerInformation.getInstance().setNumberOfPlayers(messageDetailsList.get(0));
		System.out.println("Message [" + messageNumber + "] Game Ready : Total "
				+ PlayerInformation.getInstance().getNumberOfPlayers() + " players playing the game. ");
		Map<String, Map<String, Boolean>> areaTokenMap = PlayerInformation.getInstance().initTokensInAreaList();
		PlayerInformation.getInstance().setAreaTokenMap(areaTokenMap);
		List<String> playerNameList = PlayerInfoValidation.getInstance()
				.createPlayerArray(PlayerInformation.getInstance().getNumberOfPlayers());
		Set<String> dieFaceList = PlayerInfoValidation.getInstance().createDieFaceArray();
		Set<String> terrianCharList = PlayerInfoValidation.getInstance().createTerrianCharArray();
		Set<String> terrianLocationList = PlayerInfoValidation.getInstance().createTerrianLocationArray();
		PlayerInformation.getInstance().setPlayerNameList(playerNameList);
		PlayerInformation.getInstance().setDieFaceList(dieFaceList);
		PlayerInformation.getInstance().setTerrianCharList(terrianCharList);
		PlayerInformation.getInstance().setTerrianLocationList(terrianLocationList);
		ComputerPlayerInitialization.initTerrainTokenMap(PlayerInformation.getInstance().getPlayerNameList().size());
	}

	/**
	 * Allocate remaining terrains tokens randomly. 02: <PlayerNumber> <Token
	 * Array> E.g.02:P1,2M,2F,3B,4F,7M,7B,7F
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */

	public void getPersonalTokens(String messageNumber, List<String> messageDetailsList) {

		PlayerInformation.getInstance().setPlayerName(messageDetailsList.get(0));

		System.out.println("Message [" + messageNumber + "] You are player "
				+ PlayerInformation.getInstance().getPlayerName() + " connecting to server...");
		messageDetailsList.remove(0);
		PlayerInformation.getInstance().setPersonalTokenMap(
				PlayerInformation.getInstance().addTokenInPersonalMap(messageDetailsList, personalTokenMap,
						isLeftOverToken, isSwapPlayerToken, PlayerInformation.getInstance().getAreaTokenMap()));
		PlayerInformation.getInstance().printPersonalTokens(PlayerInformation.getInstance().getPersonalTokenMap());

		// For your player number set all locations to zero
		ComputerPlayerInitialization.setAllLocationAsZeroToPlayer(PlayerInformation.getInstance().getPlayerName());
		ComputerPlayerInitialization.updatePersonalTokenMap(messageDetailsList,
				PlayerInformation.getInstance().getPlayerName(), PlayerInformation.getInstance().getPlayerNameList());
	}

	/**
	 * Inform leftover tokens to all the players after distributing tokens
	 * equally. message: 03:<OpenTokens> E.g. 03:2B
	 * 
	 * @param messageNumber
	 * @param message
	 */
	public void getLeftOverTokens(String messageNumber, List<String> messageDetailsList) {
		PlayerInformation.getInstance().setLeftOverTokens(messageDetailsList);
		System.out.print("Message [" + messageNumber + "] left over tokens, known to all players in the game :");
		isLeftOverToken = true;
		Map<String, List<String>> areaTokenMap = PlayerInformation.getInstance().addTokenInPersonalMap(
				messageDetailsList, personalTokenMap, isLeftOverToken, isSwapPlayerToken,
				PlayerInformation.getInstance().getAreaTokenMap());
		System.out.println(areaTokenMap.get(Constants.LEFT_OVER_TOKENS));
		isLeftOverToken = false;
		ComputerPlayerInitialization.updatePersonalTokenMap(messageDetailsList,
				PlayerInformation.getInstance().getPlayerName(), PlayerInformation.getInstance().getPlayerNameList());
	}

	/**
	 * If the server detects an error, this message will be sent containing a
	 * simple string describing the context of the message. message 99:text
	 * string ending in newline
	 * 
	 * @param messageNumber
	 * @param message
	 */
	public void Error(String messageNumber, List<String> messageDetailsList) {
		if ("11".equals(messageNumber)) {
			System.out.println("\nMessage [" + messageNumber + "] Player " + messageDetailsList.get(0)
					+ " is the last player left. They win by default.");
			messageDetailsList.remove(0);
			System.out.println("Treasure Locations are :" + messageDetailsList);
		} else {
			System.out.println(
					"\nMessage [" + messageNumber + "] Error please exist the game : " + messageDetailsList + "\n");
		}
	}

	/**
	 * Duringsetup if there are 4 or 5 players, the rules call for players to
	 * swap a location token. The net effect is to inform each player of a
	 * location that another player has. This message informs each player of one
	 * location token held by another player. The message is sent to both Pm &
	 * Pn. Initiator: Server message: 10:PmPn,[1-8][MFB] Pm is the owner of the
	 * token Pn is the player receiving Pm's location token information.
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 * @param message
	 */
	public void getBonusPlayerInformation(String messageNumber, List<String> messageDetailsList) {
		if (messageDetailsList.get(0).equals(PlayerInformation.getInstance().getPlayerName())) {
			System.out.print(
					"Message [" + messageNumber + "] Player " + messageDetailsList.get(1) + " knows that you have ");
			PlayerInformation.getInstance().printTerrianToken(messageDetailsList.get(2));
		} else {
			System.out.print(
					"Message [" + messageNumber + "] You know that Player " + messageDetailsList.get(0) + " has ");
			PlayerInformation.getInstance().printTerrianToken(messageDetailsList.get(2));

			System.out.println("swap player information :");
			isSwapPlayerToken = true;
			Map<String, List<String>> areaTokenMap = PlayerInformation.getInstance().addTokenInPersonalMap(
					messageDetailsList, personalTokenMap, isLeftOverToken, isSwapPlayerToken,
					PlayerInformation.getInstance().getAreaTokenMap());
			System.out.println(areaTokenMap.get(Constants.SWAP_PLAYER_TOKENS));
			isSwapPlayerToken = false;
			ComputerPlayerInitialization.updatePersonalTokenMap(messageDetailsList, messageDetailsList.get(0),
					PlayerInformation.getInstance().getPlayerNameList());
		}

	}

}
