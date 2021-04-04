package com.lol.player;

import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;

public class Player {

	/**
	 * This is main function to start the Player/Computer client.
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
	
//		System.out.println("\nEnter Your Team Name (in the format alpha, all etc.) : ");
//		String teamName = args[0]; 
//		
//		System.out.println("________________ LOOT OF LIMA!! Let's Begin the Game !_________________ ");
//		System.out.println("________________ Instructions to Play Game_____________________________ ");
//		System.out.println("1. All inputs should be given in CAPS letter");
//		System.out.println("2. Players are named as P1,P2,P3 etc.");
//		System.out.println("________________ Game Ready !! ________________________________________ ");			
//		System.out.println("\nEnter Your Player Name (in the format P1,P2,P3 etc.) : ");
//		
//		String playerNumber = args[1]; 
//		
//		String fileWritePath = "/tmp/" + teamName +"from"+ playerNumber; // Write File path
//		String fileReadPath  = "/tmp/" + teamName +"to"  + playerNumber; // Read File path
		
		String fileWritePath = "/Users/monikagadage/git/LOLPlayer-ComputerPlayer/src/PlayerFile.txt";
		String fileReadPath  =	"/Users/monikagadage/git/LOLPlayer-ComputerPlayer/src/Serverfile.txt";	
		
		PlayerInformation.getInstance().setFileReadPath(fileReadPath);
		PlayerInformation.getInstance().setFileWritePath(fileWritePath);
		PlayerInformation.getInstance().setGameOver(false); // Game over flag to set
		Utility.readFile(PlayerInformation.getInstance().getFileReadPath(), PlayerInformation.getInstance().isGameOver()); //
	}
}
