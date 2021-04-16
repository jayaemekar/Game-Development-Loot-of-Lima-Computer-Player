package com.lol.player;

import java.util.List;
import java.util.Set;

import com.lol.computer.player.AnswerDeductionLogic;
import com.lol.computer.player.ComputerPlayer;
import com.lol.computer.player.QuestionDeductionLogic;
import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;

public class GameProcessing {

	private static Integer roundNumber = 0;
	public static Integer roundCount = 0;
	private static boolean treasureGuessSent = false;

	/**
	 * This method is to parse the dice information and start the Game play
	 * 04:P1,SEM,NWB,SWF This Dice Rolled message P1-Dice rolled for Player,
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public void playersTurn(String messageNumber, List<String> messageDetailsList) {
		String playerName = messageDetailsList.get(0);
		messageDetailsList.remove(0);
		if (roundNumber == 0 || roundNumber % PlayerInformation.getInstance().getNumberOfPlayers() == 0) {
			ComputerPlayer.getInstance().setRoundCount(roundCount++);
			System.out.println("============");
			System.out.println("ROUND-" + roundCount);
			System.out.println("============");
		}
		roundNumber++;
		System.out.println("______________________");
		System.out.println("Player " + playerName + "'s turn");
		System.out.println("______________________");
		String istreasureLocFound = Constants.NO;
		if (!treasureGuessSent)
			istreasureLocFound = AnswerDeductionLogic.checkIsTreasureLocFound();

		if (Constants.YES.equalsIgnoreCase(istreasureLocFound))
			treasureGuessSent = treasureGuess();
		else {
			System.out.println("Player " + playerName + " rolled the dices " + messageDetailsList);
			if (PlayerInformation.getInstance().getPlayerName().equals(playerName) && !treasureGuessSent)
				QuestionDeductionLogic.createQuestion(messageDetailsList);
			else if (treasureGuessSent)
				System.out.println("Ignoring rolled the dices... Treasure guess sent already..!! waiting for reply...");

		}
	}

	/**
	 * Parsing question Information Message 05:NNF,NEF,A,P3
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public void getQuestionInformation(String messageNumber, List<String> messageDetailsList) {
		System.out.println("\nMessage [" + messageNumber + "] Question asked to player " + messageDetailsList.get(3)
				+ ", How many " + PlayerInformation.getInstance().getTerrianTokenInformation(messageDetailsList.get(2))
				+ " terrains between " + messageDetailsList.get(0) + " and " + messageDetailsList.get(1));
	}

	/**
	 * This method is to send the treasure Guess message to the server 07:P1,3F,6M
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 * @return
	 */

	public boolean treasureGuess() {

		Set<String> treasureLoc = ComputerPlayer.getInstance().getTreasureLoc();
		String[] treasureLocArr = treasureLoc.toArray(new String[treasureLoc.size()]);
		StringBuilder message = new StringBuilder(Constants.MESSAAGE_07).append(":");
		message.append(PlayerInformation.getInstance().getPlayerName()).append(Constants.COMMA)
				.append(treasureLocArr[0]).append(Constants.COMMA).append(treasureLocArr[1]);

		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message.toString());
		Utility.parseMessage(message.toString());
		System.out.println("____Your guess sent to server.!!______");
		return true;
	}

	public void getTreasureGuessInformation(String messageNumber, List<String> messageDetailsList) {
		System.out.println(" Player " + messageDetailsList.get(0) + " says treasures are located at "
				+ messageDetailsList.get(1) + " and " + messageDetailsList.get(2));
	}

	/**
	 * This method is used to parse the winner player and token information
	 * 08:player1,wintoken1,wintoken2 eg.08:P1,3F,6M
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public void getWinnerPlayerGuessCorrect(String messageNumber, List<String> messageDetailsList) {
		System.out.println(" Player " + messageDetailsList.get(0) + " guessed the correct treasure locations");
		if (messageDetailsList.get(0).equals(PlayerInformation.getInstance().getPlayerName()))
			System.out.println("__________YOU ARE THE WINNER !!__________");
		else
			System.out.println(
					"____________PLAYER " + messageDetailsList.get(0) + " IS THE WINNER !! __________________");

		messageDetailsList.remove(0);
		System.out.println("The treasure location are: " + messageDetailsList);
		System.out.println("\n======***====== GAME OVER !!======***======\n");
	}

	/**
	 * treasure guess was incorrect and disqualify 09:P2
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 * @return
	 */
	public boolean getDisqualifiedPlayerInformationOrInCorrectGuess(String messageNumber,
			List<String> messageDetailsList) {
		System.out.println("\nMessage [" + messageNumber + "] Player " + messageDetailsList.get(0)
				+ " has made incorrect guess. \n ");
		if (messageDetailsList.get(0).equals(PlayerInformation.getInstance().getPlayerName())) {
			System.out.println("_______You are disqualified from the game..Game is over for you !!________\n");

			System.out.println("_______Lets wait and see who wins the game________________________________\n");

			return false;
		} else {
			System.out.println("\n_________________" + messageDetailsList.get(0)
					+ " has been disqualified from the game._________________");
			treasureGuessSent = false;
			return treasureGuessSent;
		}

	}

	/**
	 * This method is used to print barrel message
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public void getReRollInformation(String messageNumber, List<String> messageDetailsList) {
		System.out.println(
				"BARREL- Player " + PlayerInformation.getInstance().getPlayerName() + " has requested to reroll Die-"
						+ messageDetailsList.get(1) + " and Die-" + messageDetailsList.get(2));

	}

}
