package com.lol.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lol.helper.PlayerInformation;

public class PlayerInfoValidation {

	private static PlayerInfoValidation playerInfoValidation = null;

	public static PlayerInfoValidation getInstance() {
		if (playerInfoValidation == null)
			playerInfoValidation = new PlayerInfoValidation();

		return playerInfoValidation;
	}

	public boolean validateDiceFace(String dieFace) {

		return PlayerInformation.getInstance().getDieFaceList().contains(dieFace);

	}

	public boolean validatePlayerName(String playerName) {
		return PlayerInformation.getInstance().getPlayerNameList().contains(playerName);
	}

	public boolean validateTerrianName(String terrianName) {
		return PlayerInformation.getInstance().getTerrianCharList().contains(terrianName);
	}

	public boolean validateTerrianLocation(String terrianLocName) {
		return PlayerInformation.getInstance().getTerrianLocationList().contains(terrianLocName);
	}

	public List<String> createPlayerArray(Integer numberOfPlayers) {
		List<String> playerNameList = new ArrayList<>();
		for (int i = 1; i <= numberOfPlayers; i++) {
			playerNameList.add(("P" + i).trim());
		}
		return playerNameList;
	}

	public Set<String> createTerrianCharArray() {
		Set<String> terrianCharList = new HashSet<>();
		terrianCharList.add("F");
		terrianCharList.add("M");
		terrianCharList.add("B");
		terrianCharList.add("A");
		return terrianCharList;
	}

	public Set<String> createTerrianLocationArray() {
		Set<String> terrianLocationList = new HashSet<>();
		terrianLocationList.add("1F");
		terrianLocationList.add("2F");
		terrianLocationList.add("3F");
		terrianLocationList.add("4F");
		terrianLocationList.add("5F");
		terrianLocationList.add("6F");
		terrianLocationList.add("7F");
		terrianLocationList.add("8F");

		terrianLocationList.add("1B");
		terrianLocationList.add("2B");
		terrianLocationList.add("3B");
		terrianLocationList.add("4B");
		terrianLocationList.add("5B");
		terrianLocationList.add("6B");
		terrianLocationList.add("7B");
		terrianLocationList.add("8B");

		terrianLocationList.add("1M");
		terrianLocationList.add("2M");
		terrianLocationList.add("3M");
		terrianLocationList.add("4M");
		terrianLocationList.add("5M");
		terrianLocationList.add("6M");
		terrianLocationList.add("7M");
		terrianLocationList.add("8M");
		return terrianLocationList;
	}

	public Set<String> createDieFaceArray() {
		Set<String> dieFaceList = new HashSet<>();
		dieFaceList.add("SWB");
		dieFaceList.add("NWB");
		dieFaceList.add("NEF");
		dieFaceList.add("SEM");

		dieFaceList.add("SWF");
		dieFaceList.add("NEM");
		dieFaceList.add("SEF");
		dieFaceList.add("NWF");

		dieFaceList.add("SWM");
		dieFaceList.add("SEB");
		dieFaceList.add("NWM");
		dieFaceList.add("NEB");

		dieFaceList.add("EEM");
		dieFaceList.add("SSB");
		dieFaceList.add("SSF");
		dieFaceList.add("EEF");

		dieFaceList.add("NNW");
		dieFaceList.add("EEW");
		dieFaceList.add("WWW");
		dieFaceList.add("NNF");

		dieFaceList.add("NNB");
		dieFaceList.add("SSM");
		dieFaceList.add("WWM");
		dieFaceList.add("WWB");

		dieFaceList.add("SWB");
		dieFaceList.add("SEF");
		dieFaceList.add("NNM");
		dieFaceList.add("WWF");

		dieFaceList.add("NEM");
		dieFaceList.add("NNM");
		dieFaceList.add("SSW");
		dieFaceList.add("SSM");

		dieFaceList.add("EEB");
		dieFaceList.add("EEB");
		dieFaceList.add("WWF");
		dieFaceList.add("NWB");

		return dieFaceList;

	}

}
