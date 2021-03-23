package com.lol.player;

import java.util.Scanner;

import com.lol.helper.PlayerInformation;
import com.lol.helper.Utility;

public class Player {

	/**
	 * This is main function to call the client.
	 * ALLall
	 * p
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nEnter Your Team Name (in the format alpha, all etc.) : ");
		String teamName = sc.nextLine();
		
		System.out.println("________________ LOOT OF LIMA!! Let's Begin the Game !_________________ ");
		System.out.println("________________ Instructions to Play Game_____________________________ ");
		System.out.println("________________ 1. All inputs should be given in CAPS letter__________ ");
		System.out.println("________________ 2. Players are named as P1,P2,P3 etc._________________ ");
		System.out.println("________________ Game Ready !! ________________________________________ ");
				
		System.out.println("\nEnter Your Player Name (in the format P1,P2,P3 etc.) : ");
		String playerNumber = sc.nextLine();
		
		String fileWritePath = "/tmp/" + teamName +"from"+ playerNumber; // Write File path
		String fileReadPath  = "/tmp/" + teamName +"to"  + playerNumber; // Read File path
		
		//fileReadPath = "F:/LootOfLima/LOLPlayer-ComputerPlayer/src/ServerFile.txt";
		//fileWritePath = "F:/LootOfLima/LOLPlayer-ComputerPlayer/src/PlayerFile.txt";
		PlayerInformation.getInstance().setFileReadPath(fileReadPath);
		PlayerInformation.getInstance().setFileWritePath(fileWritePath);
		PlayerInformation.getInstance().setGameOver(false); // Game over flag to set
		Utility.readFile(PlayerInformation.getInstance().getFileReadPath(), PlayerInformation.getInstance().isGameOver()); //
	}
}
