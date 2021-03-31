package com.lol.helper;

import static java.nio.file.StandardOpenOption.WRITE;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lol.constant.Constants;
import com.lol.player.GameInitialization;
import com.lol.player.GameProcessing;

public class Utility {

	public static GameInitialization gameIntialization = new GameInitialization();
	public static GameProcessing gameProcessing = new GameProcessing();

	/**
	 * Read the server files
	 * 
	 * @param filename
	 * @param isGameOver
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void readFile(String filename, boolean canBreak) throws InterruptedException, IOException {
		canBreak = false;
		String line;
		try {
			LineNumberReader lnr =new LineNumberReader(new FileReader(filename));
			while (!canBreak) {
				line = lnr.readLine();
				if (line == null || line.isEmpty()) {				
					Thread.sleep(3000);
					continue;
				}
				canBreak = parseMessage(line);
				if (canBreak)
					break;
			}
			lnr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write into the file
	 * 
	 * @param filePath
	 * @param message
	 */
	public static void writeFile(String filePath, String message) {
		try {
		/*	FileWriter fw = new FileWriter(filePath, true);
			fw.write(message + "\n");
			fw.close();*/
			Path path = Paths.get(filePath);
			OutputStream outputStream = Files.newOutputStream(path, WRITE);
			outputStream.write(message.getBytes());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse messages
	 * 
	 * @param message
	 * @return
	 */
	public static boolean parseMessage(String message) {

		String[] messageArray = message.split(":");
		String[] messageDetails = messageArray[1].split(",");
		String messageNumber = messageArray[0];
		List<String> messageDetailsList = new ArrayList<>(Arrays.asList(messageDetails));

		if (Constants.MESSAAGE_01.equals(messageNumber))
			gameIntialization.gameReadyInformation(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_02.equals(messageNumber))
			gameIntialization.getPersonalTokens(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_03.equals(messageNumber))
			gameIntialization.getLeftOverTokens(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_10.equals(messageNumber))
			gameIntialization.getBonusPlayerInformation(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_04.equals(messageNumber))
			gameProcessing.playersTurn(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_05.equals(messageNumber))
			gameProcessing.getQuestionInformation(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_06.equals(messageNumber))
			gameProcessing.getAnswerInformation(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_07.equals(messageNumber))
			gameProcessing.getTreasureGuessInformation(messageNumber, messageDetailsList);
		else if (Constants.MESSAAGE_08.equals(messageNumber)) {
			gameProcessing.getWinnerPlayerGuessCorrect(messageNumber, messageDetailsList);
			PlayerInformation.getInstance().setGameOver(true);
			return true;
		} else if (Constants.MESSAAGE_09.equals(messageNumber)) {
			boolean flag = gameProcessing.getDisqualifiedPlayerInformationOrInCorrectGuess(messageNumber,
					messageDetailsList);
			PlayerInformation.getInstance().setGameOver(flag);
			return flag;
		} else {
			gameIntialization.Error(messageNumber, messageDetailsList);
			PlayerInformation.getInstance().setGameOver(true);
			return true;
		}
		return false;
	}
}
