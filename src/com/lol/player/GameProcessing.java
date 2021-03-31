package com.lol.player;

import java.util.List;
import java.util.Set;

import com.lol.computer.player.ComputerPlayer;
import com.lol.computer.player.ComputerPlayerDeductionLogic;
import com.lol.computer.player.QuestionDeductionLogic;
import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;
import com.lol.validation.PlayerInfoValidation;

public class GameProcessing {

	private static Integer roundNumber = 0;
	private static Integer roundCount = 0;
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
			roundCount++;
			System.out.println(
					"\n====== ====== ====== ====== ROUND-" + roundCount + " ====== ====== ====== ====== ======\n");
		}
		roundNumber++;

		System.out.println("_______________________Player " + playerName + "'s turn____________________________");
		String istreasureLocFound = Constants.NO;
		if (!treasureGuessSent) {
			istreasureLocFound = ComputerPlayerDeductionLogic.checkIsTreasureLocFound();
			System.out.println("\nGuess the Treasure Locations???(YES/NO)???  :: " + istreasureLocFound);
		}

		if (Constants.YES.equalsIgnoreCase(istreasureLocFound))
			treasureGuessSent = treasureGuess();
		else {
			System.out.println(
					"Message [" + messageNumber + "] Player " + playerName + " rolled the dices " + messageDetailsList);
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
	 * this method is to get the answer information 06:NNF,NEF,F,2,P3,P1
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public void getAnswerInformation(String messageNumber, List<String> messageDetailsList) {
		ComputerPlayerDeductionLogic.processAnswerMessage(messageDetailsList);
		System.out.println("Message [" + messageNumber + "] Player " + messageDetailsList.get(4) + " has "
				+ messageDetailsList.get(3) + " "
				+ PlayerInformation.getInstance().getTerrianTokenInformation(messageDetailsList.get(2))
				+ " terrain between "
				+ PlayerInformation.getInstance().getDirectionInformation(messageDetailsList.get(0)) + " and "
				+ PlayerInformation.getInstance().getDirectionInformation(messageDetailsList.get(1)) + "\n");

	}

	/**
	 * This method is to send the treasure Guess message to the server
	 * 07:P1,3F,6M
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 * @return
	 */

	public boolean treasureGuess() {
		StringBuilder message = new StringBuilder("07:");
		Set<String> treasureLoc = ComputerPlayer.getInstance().getTreasureLoc();
		String[] treasureLocArr = treasureLoc.toArray(new String[treasureLoc.size()]);

		message.append(PlayerInformation.getInstance().getPlayerName()).append(Constants.COMMA);
		String guessOne = treasureLocArr[0];
		System.out.println("First guess identified as :: " + guessOne);
		if (PlayerInfoValidation.getInstance().validateTerrianLocation(guessOne)) {
			message.append(guessOne).append(Constants.COMMA);
		} else {
			System.out.println("\n ====== Invalid terrian token create message again ====== \n");
			treasureGuess();
			return false;
		}

		String guessTwo = treasureLocArr[1];
		System.out.println("Second guess identified as :: " + guessTwo);
		if (PlayerInfoValidation.getInstance().validateTerrianLocation(guessTwo)) {
			message.append(guessTwo);
		} else {
			System.out.println("\n ====== Invalid terrian token create message again ====== \n");
			treasureGuess();
			return false;
		}

		Utility.writeFile(PlayerInformation.getInstance().getFileWritePath(), message.toString());
		Utility.parseMessage(message.toString());
		System.out.println("\n ====== ====== ====== ====== Your guess sent to server ====== ====== ====== ======\n");
		return true;
	}

	public void getTreasureGuessInformation(String messageNumber, List<String> messageDetailsList) {
		System.out.println("\nMessage [" + messageNumber + "] Player " + messageDetailsList.get(0)
				+ " says treasures are located at " + messageDetailsList.get(1) + " and " + messageDetailsList.get(2));
	}

	/**
	 * This method is used to parse the winner player and token information
	 * 08:player1,wintoken1,wintoken2 eg.08:P1,3F,6M
	 * 
	 * @param messageNumber
	 * @param messageDetailsList
	 */
	public void getWinnerPlayerGuessCorrect(String messageNumber, List<String> messageDetailsList) {
		System.out.println("\nMessage [" + messageNumber + "] Player " + messageDetailsList.get(0)
				+ " guessed the correct treasure locations\n");
		if (messageDetailsList.get(0).equals(PlayerInformation.getInstance().getPlayerName())) {
			System.out.println("\n\n====== ====== ====== ====== YOU ARE THE WINNER !!====== ====== ====== ======");
		} else {

			System.out.println(
					"\n======***====== PLAYER " + messageDetailsList.get(0) + " IS THE WINNER !! ======***======");
		}
		messageDetailsList.remove(0);
		System.out.println("\nThe treasure location are: " + messageDetailsList);
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

}
