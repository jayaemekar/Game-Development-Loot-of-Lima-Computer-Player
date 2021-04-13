package com.lol.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lol.constant.Constants;

public class PlayerInformation {

	private String numberOfPlayers;
	private List<String> personalTokens;
	private List<String> leftOverTokens;
	private String bonusPlayerToken;
	private String playerName;
	private Map<String, List<String>> personalTokenMap;
	Map<String, Map<String, Boolean>> areaTokenMap;
	List<String> playerNameList;
	Set<String> dieFaceList;
	Set<String> terrianCharList;
	Set<String> terrianLocationList;
	String fileReadPath;
	String fileWritePath;
	boolean isGameOver = false;

	private static volatile PlayerInformation playerInformation;

	public PlayerInformation(String playerName) {
		PlayerInformation.getInstance().setPlayerName(playerName);
	}

	private PlayerInformation() {
		// private constructor
	}

	public static PlayerInformation getInstance() {
		if (playerInformation == null) {
			synchronized (PlayerInformation.class) {
				if (playerInformation == null) {
					playerInformation = new PlayerInformation();
				}
			}
		}
		return playerInformation;
	}

	public int getNumberOfPlayers() {
		return Integer.valueOf(numberOfPlayers);
	}

	public void setNumberOfPlayers(String numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public List<String> getPersonalTokens() {
		return personalTokens;
	}

	public void setPersonalTokens(List<String> personalTokens) {
		this.personalTokens = personalTokens;
	}

	public List<String> getLeftOverTokens() {
		return leftOverTokens;
	}

	public void setLeftOverTokens(List<String> leftOverTokens) {
		this.leftOverTokens = leftOverTokens;
	}

	public String getBonusPlayerToken() {
		return bonusPlayerToken;
	}

	public void setBonusPlayerToken(String bonusPlayerToken) {
		this.bonusPlayerToken = bonusPlayerToken;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Map<String, List<String>> getPersonalTokenMap() {
		return personalTokenMap;
	}

	public void setPersonalTokenMap(Map<String, List<String>> personalTokenMap) {
		this.personalTokenMap = personalTokenMap;
	}

	public Map<String, Map<String, Boolean>> getAreaTokenMap() {
		return areaTokenMap;
	}

	public void setAreaTokenMap(Map<String, Map<String, Boolean>> areaTokenMap) {
		this.areaTokenMap = areaTokenMap;
	}

	public List<String> getPlayerNameList() {
		return playerNameList;
	}

	public void setPlayerNameList(List<String> playerNameList2) {
		this.playerNameList = playerNameList2;
	}

	public Set<String> getDieFaceList() {
		return dieFaceList;
	}

	public void setDieFaceList(Set<String> dieFaceList) {
		this.dieFaceList = dieFaceList;
	}

	public Set<String> getTerrianCharList() {
		return terrianCharList;
	}

	public void setTerrianCharList(Set<String> terrianCharList) {
		this.terrianCharList = terrianCharList;
	}

	public Set<String> getTerrianLocationList() {
		return terrianLocationList;
	}

	public void setTerrianLocationList(Set<String> terrianLocationList) {
		this.terrianLocationList = terrianLocationList;
	}

	public String getFileReadPath() {
		return fileReadPath;
	}

	public void setFileReadPath(String fileReadPath) {
		this.fileReadPath = fileReadPath;
	}

	public String getFileWritePath() {
		return fileWritePath;
	}

	public void setFileWritePath(String fileWritePath) {
		this.fileWritePath = fileWritePath;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	/**
	 * This method is used to save the information of players personal token
	 * 
	 * @param messageDetailsList
	 * @param personalTokenMap
	 * @param isLeftOverToken
	 * @param isSwapPlayerToken
	 * @param areaTokenMap
	 * @return
	 */
	public Map<String, List<String>> addTokenInPersonalMap(List<String> messageDetailsList,
			Map<String, List<String>> personalTokenMap, boolean isLeftOverToken, boolean isSwapPlayerToken,
			Map<String, Map<String, Boolean>> areaTokenMap) {

		List<String> mountainsList = new ArrayList<>();
		List<String> beachList = new ArrayList<>();
		List<String> forestList = new ArrayList<>();
		List<String> leftOverTokenList = new ArrayList<>();
		List<String> swapPlayerTokenList = new ArrayList<>();
		for (String tokens : messageDetailsList) {
			String terrianToken = String.valueOf(tokens.charAt(1));
			if (isLeftOverToken) {
				leftOverTokenList.add(tokens);
				personalTokenMap.put(Constants.LEFT_OVER_TOKENS, leftOverTokenList);
			} else if (isSwapPlayerToken) {
				swapPlayerTokenList.add(tokens);
				personalTokenMap.put(Constants.SWAP_PLAYER_TOKENS, swapPlayerTokenList);
			} else if (Constants.MOUNTAINS_CHAR.equals(terrianToken)) {
				mountainsList.add(tokens);
				personalTokenMap.put(Constants.MOUNTAINS, mountainsList);
				setAreaTokenMap(updateTokensInAreaMap(tokens, areaTokenMap));

			} else if (Constants.BEACH_CHAR.equals(terrianToken)) {
				beachList.add(tokens);
				personalTokenMap.put(Constants.BEACH, beachList);
				setAreaTokenMap(updateTokensInAreaMap(tokens, areaTokenMap));
			} else if (Constants.FOREST_CHAR.equals(terrianToken)) {
				forestList.add(tokens);
				personalTokenMap.put(Constants.FOREST, forestList);
				setAreaTokenMap(updateTokensInAreaMap(tokens, areaTokenMap));
			}
		}
		return personalTokenMap;
	}

	private Map<String, Map<String, Boolean>> updateTokensInAreaMap(String tokens,
			Map<String, Map<String, Boolean>> areaTokenMap) {
		String area = tokens.substring(0, 1);

		if ("1".equals(area)) {
			updateTokenValues(Constants.AREA_1, tokens, areaTokenMap);
		} else if ("2".equals(area)) {
			updateTokenValues(Constants.AREA_2, tokens, areaTokenMap);
		} else if ("3".equals(area)) {
			updateTokenValues(Constants.AREA_3, tokens, areaTokenMap);
		} else if ("4".equals(area)) {
			updateTokenValues(Constants.AREA_4, tokens, areaTokenMap);
		} else if ("5".equals(area)) {
			updateTokenValues(Constants.AREA_5, tokens, areaTokenMap);
		} else if ("6".equals(area)) {
			updateTokenValues(Constants.AREA_6, tokens, areaTokenMap);
		} else if ("7".equals(area)) {
			updateTokenValues(Constants.AREA_7, tokens, areaTokenMap);
		} else if ("8".equals(area)) {
			updateTokenValues(Constants.AREA_8, tokens, areaTokenMap);
		}

		return areaTokenMap;
	}

	private void updateTokenValues(String area, String tokens, Map<String, Map<String, Boolean>> areaTokenMap) {
		Map<String, Boolean> areaMap = areaTokenMap.get(area);
		for (String key : areaMap.keySet()) {
			if (tokens.equals(key))
				areaMap.put(tokens, true);
		}
		areaTokenMap.put(area, areaMap);

	}

	public Map<String, Map<String, Boolean>> initTokensInAreaList() {
		Map<String, Map<String, Boolean>> areaTokenMap = new HashMap<>();
		intializeAreaTokenMap("1F", "1B", "1M", Constants.AREA_1, areaTokenMap);
		intializeAreaTokenMap("2F", "2B", "2M", Constants.AREA_2, areaTokenMap);
		intializeAreaTokenMap("3F", "3B", "3M", Constants.AREA_3, areaTokenMap);
		intializeAreaTokenMap("4F", "4B", "4M", Constants.AREA_4, areaTokenMap);
		intializeAreaTokenMap("5F", "5B", "5M", Constants.AREA_5, areaTokenMap);
		intializeAreaTokenMap("6F", "6B", "6M", Constants.AREA_6, areaTokenMap);
		intializeAreaTokenMap("7F", "7B", "7M", Constants.AREA_7, areaTokenMap);
		intializeAreaTokenMap("8F", "8B", "8M", Constants.AREA_8, areaTokenMap);
		return areaTokenMap;
	}

	private void intializeAreaTokenMap(String forest, String mountain, String beach, String direction,
			Map<String, Map<String, Boolean>> areaTokenMap) {
		Map<String, Boolean> terrianMap = new HashMap<>();
		terrianMap.put(forest, false);
		terrianMap.put(beach, false);
		terrianMap.put(mountain, false);
		areaTokenMap.put(direction, terrianMap);

	}

	public void printPersonalTokens(Map<String, List<String>> personalTokenMap) {
		for (String key : personalTokenMap.keySet()) {
			System.out.println(key.trim() + ":" + personalTokenMap.get(key));
		}
	}

	public void printTerrianToken(String tokens) {
		String terrianToken = String.valueOf(tokens.charAt(1));
		if (Constants.MOUNTAINS_CHAR.equals(terrianToken)) {
			System.out.println(Constants.MOUNTAINS + " :" + tokens.charAt(0));
		} else if (Constants.BEACH_CHAR.equals(terrianToken)) {
			System.out.println(Constants.BEACH + " :" + tokens.charAt(0));
		} else {
			System.out.println(Constants.FOREST + " :" + tokens.charAt(0));
		}
	}

	public String getDirectionInformation(String dieFace) {
		String direction = dieFace.substring(0, 2);
		if (Constants.NORTH_CHAR.equals(direction)) {
			return Constants.NORTH;
		} else if (Constants.NORTH_EAST_CHAR.equals(direction)) {
			return Constants.NORTH_EAST;
		} else if (Constants.NORTH_WEST_CHAR.equals(direction)) {
			return Constants.NORTH_WEST;
		} else if (Constants.SOUTH_CHAR.equals(direction)) {
			return Constants.SOUTH;
		} else if (Constants.SOUTH_EAST_CHAR.equals(direction)) {
			return Constants.SOUTH_EAST;
		} else if (Constants.SOUTH_WEST_CHAR.equals(direction)) {
			return Constants.SOUTH_WEST;
		} else if (Constants.EAST_CHAR.equals(direction)) {
			return Constants.EAST;
		} else if (Constants.WEST_CHAR.equals(direction)) {
			return Constants.WEST;
		} else {
			return Constants.INAVALID_DIRECTION;
		}
	}

	public String getTerrianTokenInformation(String dieFace) {
		String terrianToken;
		if (dieFace.toCharArray().length == 3)
			terrianToken = String.valueOf(dieFace.charAt(2));
		else
			terrianToken = dieFace;
		if (Constants.MOUNTAINS_CHAR.equals(terrianToken)) {
			return Constants.MOUNTAINS;
		} else if (Constants.BEACH_CHAR.equals(terrianToken)) {
			return Constants.BEACH;
		} else if (Constants.FOREST_CHAR.equals(terrianToken)) {
			return Constants.FOREST;
		} else if (Constants.WILD_CHAR.equals(terrianToken)) {
			return Constants.WILD;
		} else if (Constants.ALL_CHAR.equals(terrianToken)) {
			return Constants.ALL;
		} else {
			return Constants.INVALID_TERRIAN_TYPE;
		}

	}

}
