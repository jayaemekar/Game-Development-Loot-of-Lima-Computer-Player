package com.lol.computer.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class ComputerPlayer {

	Map<String, Boolean> playerTokenArray;

	Set<String> notTreasureLoc;

	Set<String> canbeTreasureLoc;

	Set<String> treasureLoc;

	List<Integer> defaultLocVal;

	Map<String, Map<String, Integer>> allPlayerTrrianMap;

	Map<String, Integer> playerObj;

	Set<String> allTerriansList;
	Map<String, Integer> dirIntMap;
	Map<Integer, Set<String>> directionIntegerMap;
	Map<String, Map<String, Integer>> deducedPlayerTokenMap;
	
	private static ComputerPlayer computerPlayer = null;

	private ComputerPlayer() {
		// private
	}

	public static ComputerPlayer getInstance() {
		if (computerPlayer == null)
			computerPlayer = new ComputerPlayer();

		return computerPlayer;
	}

	public Map<String, Boolean> getPlayerTokenArray() {
		return playerTokenArray;
	}

	public void setPlayerTokenArray(Map<String, Boolean> playerTokenArray) {
		this.playerTokenArray = playerTokenArray;
	}

	public Set<String> getNotTreasureLoc() {
		return notTreasureLoc;
	}

	public void setNotTreasureLoc(Set<String> notTreasureLoc) {
		this.notTreasureLoc = notTreasureLoc;
	}

	public Set<String> getCanbeTreasureLoc() {
		return canbeTreasureLoc;
	}

	public void setCanbeTreasureLoc(Set<String> canbeTreasureLoc) {
		this.canbeTreasureLoc = canbeTreasureLoc;
	}

	public Set<String> getTreasureLoc() {
		return treasureLoc;
	}

	public void setTreasureLoc(Set<String> treasureLoc) {
		this.treasureLoc = treasureLoc;
	}

	public List<Integer> getDefaultLocVal() {
		return defaultLocVal;
	}

	public ArrayList<Integer> setDefaultLocVal(ArrayList<Integer> arrayList, Integer playerNumber) {

		for (int i = 0; i < playerNumber; i++)
			arrayList.add(-1);
		this.defaultLocVal = arrayList;
		return arrayList;
	}

	public Map<String, Map<String, Integer>> getAllPlayerTrrianMap() {
		return allPlayerTrrianMap;
	}

	public void setAllPlayerTrrianMap(Map<String, Map<String, Integer>> terrainList) {
		this.allPlayerTrrianMap = terrainList;
	}

	public Map<String, Integer> getPlayerObj() {
		return playerObj;
	}

	public Map<String, Integer> setPlayerObj(Map<String, Integer> playerObj, Integer playerNumber) {
		for (int i = 1; i <= playerNumber; i++)
			playerObj.put("P" + i, -1);
		this.playerObj = playerObj;
		return playerObj;
	}

	public Set<String> getAllTerriansList() {
		Set<String> allTerrianList = new HashSet<>();
		allTerrianList.add(Constants.MOUNTAIN_1);
		allTerrianList.add(Constants.MOUNTAIN_2);
		allTerrianList.add(Constants.MOUNTAIN_3);
		allTerrianList.add(Constants.MOUNTAIN_4);
		allTerrianList.add(Constants.MOUNTAIN_5);
		allTerrianList.add(Constants.MOUNTAIN_6);
		allTerrianList.add(Constants.MOUNTAIN_7);
		allTerrianList.add(Constants.MOUNTAIN_8);
		allTerrianList.add(Constants.BEACH_1);
		allTerrianList.add(Constants.BEACH_2);
		allTerrianList.add(Constants.BEACH_3);
		allTerrianList.add(Constants.BEACH_4);
		allTerrianList.add(Constants.BEACH_5);
		allTerrianList.add(Constants.BEACH_6);
		allTerrianList.add(Constants.BEACH_7);
		allTerrianList.add(Constants.BEACH_8);
		allTerrianList.add(Constants.FOREST_1);
		allTerrianList.add(Constants.FOREST_2);
		allTerrianList.add(Constants.FOREST_3);
		allTerrianList.add(Constants.FOREST_4);
		allTerrianList.add(Constants.FOREST_5);
		allTerrianList.add(Constants.FOREST_6);
		allTerrianList.add(Constants.FOREST_7);
		allTerrianList.add(Constants.FOREST_8);
		return allTerrianList;
	}

	public void setAllTerriansList(Set<String> allTerrianList) {
		allTerrianList.add(Constants.MOUNTAIN_1);
		allTerrianList.add(Constants.MOUNTAIN_2);
		allTerrianList.add(Constants.MOUNTAIN_3);
		allTerrianList.add(Constants.MOUNTAIN_4);
		allTerrianList.add(Constants.MOUNTAIN_5);
		allTerrianList.add(Constants.MOUNTAIN_6);
		allTerrianList.add(Constants.MOUNTAIN_7);
		allTerrianList.add(Constants.MOUNTAIN_8);
		allTerrianList.add(Constants.BEACH_1);
		allTerrianList.add(Constants.BEACH_2);
		allTerrianList.add(Constants.BEACH_3);
		allTerrianList.add(Constants.BEACH_4);
		allTerrianList.add(Constants.BEACH_5);
		allTerrianList.add(Constants.BEACH_6);
		allTerrianList.add(Constants.BEACH_7);
		allTerrianList.add(Constants.BEACH_8);
		allTerrianList.add(Constants.FOREST_1);
		allTerrianList.add(Constants.FOREST_2);
		allTerrianList.add(Constants.FOREST_3);
		allTerrianList.add(Constants.FOREST_4);
		allTerrianList.add(Constants.FOREST_5);
		allTerrianList.add(Constants.FOREST_6);
		allTerrianList.add(Constants.FOREST_7);
		allTerrianList.add(Constants.FOREST_8);
		this.allTerriansList = allTerrianList;
	}

	public Map<String, Integer> getDirIntMap() {
		return dirIntMap;
	}

	public void setDirIntMap(Map<String, Integer> dirIntMap) {
		this.dirIntMap = dirIntMap;
	}

	public Map<Integer, Set<String>> getDirectionIntegerMap() {
		return directionIntegerMap;
	}

	public void setDirectionIntegerMap(Map<Integer, Set<String>> directionIntegerMap) {
		this.directionIntegerMap = directionIntegerMap;
	}

	public static ComputerPlayer getComputerPlayer() {
		return computerPlayer;
	}

	public static void setComputerPlayer(ComputerPlayer computerPlayer) {
		ComputerPlayer.computerPlayer = computerPlayer;
	}

	public void setDefaultLocVal(List<Integer> defaultLocVal) {
		this.defaultLocVal = defaultLocVal;
	}

	public void setPlayerObj(Map<String, Integer> playerObj) {
		this.playerObj = playerObj;
	}

	public Map<String, Map<String, Integer>> getDeducedPlayerTokenMap() {
		return deducedPlayerTokenMap;
	}

	public void setDeducedPlayerTokenMap(Map<String, Map<String, Integer>> deducedPlayerTokenMap) {
		this.deducedPlayerTokenMap = deducedPlayerTokenMap;
	}

}
