package com.lol.computer.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Node {
	Map<String, Map<String, Integer>> terrainList;
	String direction;
	String number;
	Node next;

	public Node(Map<String, Map<String, Integer>> terrainList, String direction, String number) {
		this.terrainList = terrainList;
		this.direction = direction;
		this.number = number;
	}
}

public class ComputerPlayer {
	private Node head = null;
	private Node tail = null;
	private Map<String, Boolean> playerTokenArray;
	private Set<String> notTreasureLoc = new HashSet<>();;
	private Set<String> treasureLoc = new HashSet<>();;
	private Map<String, Map<String, Integer>> allPlayerTrrianMap;
	private Map<String, Map<String, Integer>> deducedPlayerTokenMap;
	private Set<String> deducedForestLoc = new HashSet<>();
	private Set<String> deducedBeachLoc = new HashSet<>();
	private Set<String> deducedMountainLoc = new HashSet<>();
	private Node currentNode;
	private Map<String, Integer> playerObj;
	private static ComputerPlayer computerPlayer = null;
	private Map<String, List<List<String>>> allTentativeToken;
	private Map<String, Map<String, List<List<String>>>> TentativeToken;
	private Map<Integer, Set<String>> NorthSet = new HashMap<>();
	private Map<Integer, Set<String>> NorthEastSet = new HashMap<>();
	private Map<Integer, Set<String>> EastSet = new HashMap<>();
	private Map<Integer, Set<String>> SouthEastSet = new HashMap<>();

	private Map<Integer, Set<String>> SouthSet = new HashMap<>();
	private Map<Integer, Set<String>> SouthWestSet = new HashMap<>();
	private Map<Integer, Set<String>> WestSet = new HashMap<>();
	private Map<Integer, Set<String>> NorthWestSet = new HashMap<>();

	public Map<Integer, Set<String>> getNorthSet() {
		return NorthSet;
	}

	public void setNorthSet(Map<Integer, Set<String>> northSet) {
		NorthSet = northSet;
	}

	public Map<Integer, Set<String>> getNorthEastSet() {
		return NorthEastSet;
	}

	public void setNorthEastSet(Map<Integer, Set<String>> northEastSet) {
		NorthEastSet = northEastSet;
	}

	public Map<Integer, Set<String>> getEastSet() {
		return EastSet;
	}

	public void setEastSet(Map<Integer, Set<String>> eastSet) {
		EastSet = eastSet;
	}

	public Map<Integer, Set<String>> getSouthEastSet() {
		return SouthEastSet;
	}

	public void setSouthEastSet(Map<Integer, Set<String>> southEastSet) {
		SouthEastSet = southEastSet;
	}

	public Map<Integer, Set<String>> getSouthSet() {
		return SouthSet;
	}

	public void setSouthSet(Map<Integer, Set<String>> southSet) {
		SouthSet = southSet;
	}

	public Map<Integer, Set<String>> getSouthWestSet() {
		return SouthWestSet;
	}

	public void setSouthWestSet(Map<Integer, Set<String>> southWestSet) {
		SouthWestSet = southWestSet;
	}

	public Map<Integer, Set<String>> getWestSet() {
		return WestSet;
	}

	public void setWestSet(Map<Integer, Set<String>> westSet) {
		WestSet = westSet;
	}

	public Map<Integer, Set<String>> getNorthWestSet() {
		return NorthWestSet;
	}

	public void setNorthWestSet(Map<Integer, Set<String>> northWestSet) {
		NorthWestSet = northWestSet;
	}

	private ComputerPlayer() {
		// private constructor
	}

	public static ComputerPlayer getInstance() {
		if (computerPlayer == null)
			computerPlayer = new ComputerPlayer();
		return computerPlayer;
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

	public Map<String, Boolean> getPlayerTokenArray() {
		return playerTokenArray;
	}

	public void setPlayerTokenArray(Map<String, Boolean> playerTokenArray) {
		this.playerTokenArray = playerTokenArray;
	}

	public Set<String> getNotTreasureLoc() {
		return notTreasureLoc;
	}

	public Set<String> setNotTreasureLoc(Set<String> notTreasureLoc) {
		return this.notTreasureLoc = notTreasureLoc;
	}

	public Set<String> getTreasureLoc() {
		return treasureLoc;
	}

	public Set<String> setTreasureLoc(Set<String> treasureLoc) {
		return this.treasureLoc = treasureLoc;
	}

	public Map<String, Map<String, Integer>> getAllPlayerTrrianMap() {
		return allPlayerTrrianMap;
	}

	public void setAllPlayerTrrianMap(Map<String, Map<String, Integer>> terrainList) {
		this.allPlayerTrrianMap = terrainList;
	}

	public static ComputerPlayer getComputerPlayer() {
		return computerPlayer;
	}

	public static void setComputerPlayer(ComputerPlayer computerPlayer) {
		ComputerPlayer.computerPlayer = computerPlayer;
	}

	public Map<String, Map<String, Integer>> getDeducedPlayerTokenMap() {
		return deducedPlayerTokenMap;
	}

	public void setDeducedPlayerTokenMap(Map<String, Map<String, Integer>> deducedPlayerTokenMap) {
		this.deducedPlayerTokenMap = deducedPlayerTokenMap;
	}

	public Set<String> getDeducedForestLoc() {
		return deducedForestLoc;
	}

	public void setDeducedForestLoc(Set<String> deducedForestLoc) {
		this.deducedForestLoc = deducedForestLoc;
	}

	public Set<String> getDeducedBeachLoc() {
		return deducedBeachLoc;
	}

	public void setDeducedBeachLoc(Set<String> deducedBeachLoc) {
		this.deducedBeachLoc = deducedBeachLoc;
	}

	public Set<String> getDeducedMountainLoc() {
		return deducedMountainLoc;
	}

	public void setDeducedMountainLoc(Set<String> deducedMountainLoc) {
		this.deducedMountainLoc = deducedMountainLoc;
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	// This function will add the new node at the end of the list.
	public void add(Map<String, Map<String, Integer>> terrainList, String direction, int number) {
		// Create new node
		Node newNode = new Node(terrainList, direction, String.valueOf(number));
		// Checks if the list is empty.
		if (head == null) {
			head = newNode;
			tail = newNode;
			newNode.next = head;
		} else {
			tail.next = newNode;
			tail = newNode;
			tail.next = head;
		}
	}

	public Map<String, List<List<String>>> getAllTentativeToken() {
		return allTentativeToken;
	}

	public void setAllTentativeToken(Map<String, List<List<String>>> allTentativeToken) {
		this.allTentativeToken = allTentativeToken;
	}

	public void setPlayerObj(Map<String, Integer> playerObj) {
		this.playerObj = playerObj;
	}

	public void display() {
		Node current = head;
		if (head == null) {
			System.out.println("List is empty");
		} else {
			System.out.println("Nodes of the circular linked list: ");
			do {

				System.out.print(" " + current.terrainList);
				System.out.print(" " + current.direction);
				System.out.print(" " + current.number);

				System.out.print("\n");
				current = current.next;
			} while (current != head);
			System.out.println();
		}
	}

	public Node createNode(String direction) {
		Node current = head;
		Node directionNode = null;
		do {
			if (current.direction.equals(direction)) {
				directionNode = current;
				break;
			}
			current = current.next;
		} while (current != head);
		return directionNode;
	}

	public Map<String, Map<String, List<List<String>>>> getTentativeToken() {
		return TentativeToken;
	}

	public void setTentativeToken(Map<String, Map<String, List<List<String>>>> tentativeToken) {
		TentativeToken = tentativeToken;
	}

}
