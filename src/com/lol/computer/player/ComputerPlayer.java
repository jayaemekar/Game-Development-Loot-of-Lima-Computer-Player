package com.lol.computer.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the Node class.
 *
 */
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
/**
 * This is singleton class for Computer player
 */
public class ComputerPlayer {
	private Node head = null;
	private Node tail = null;

	// Player token map
	private Set<String> notTreasureLoc = new HashSet<>();;
	private Set<String> treasureLoc = new HashSet<>();;
	private Map<String, Map<String, Integer>> allPlayerTrrianMap = new HashMap<>();
	private Map<String, Integer> playerObj;
	private Map<String, Map<String, List<List<String>>>> tentativeToken;

	// special abilities
	private Boolean ShovelFlag = true;
	private Boolean PistolFlag = false;
	private Boolean BarrelFlag = false;

	private Integer roundCount = 0;

	// Singleton Computer player class
	private static volatile ComputerPlayer computerPlayer = null;

	private ComputerPlayer() {
		// private constructor
	}
	//singleton object creation
	public static ComputerPlayer getInstance() {
		if (computerPlayer == null) {
			synchronized (ComputerPlayer.class) {
				if (computerPlayer == null) {
					computerPlayer = new ComputerPlayer();
				}
			}
		}
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

	public void setPlayerObj(Map<String, Integer> playerObj) {
		this.playerObj = playerObj;
	}

	/**
	 * This function is used to display the linked list
	 */
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

	/**
	 * This is used to create the new node
	 * 
	 * @param direction
	 * @return
	 */
	public Node createNode(String direction) {
		Node current = head;
		Node directionNode = null;
		do {
			if (current.direction != null && current.direction.equals(direction)) {
				directionNode = current;
				break;
			}
			current = current.next;
		} while (current != head);
		return directionNode;
	}

	public Map<String, Map<String, List<List<String>>>> getTentativeToken() {
		return tentativeToken;
	}

	public void setTentativeToken(Map<String, Map<String, List<List<String>>>> tentativeToken) {
		this.tentativeToken = tentativeToken;
	}

	public Integer getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(Integer roundCount) {
		this.roundCount = roundCount;
	}

	public Boolean getShovelFlagStatus() {
		return ShovelFlag;
	}

	public Boolean getPistolFlagStatus() {
		return PistolFlag;
	}

	public Boolean getBarrelFlagStatus() {
		return BarrelFlag;
	}

	public void setShovelFlag() {
		ShovelFlag = false;
	}

	public void setPistolFlag() {
		PistolFlag = true;
	}

	public void setBarrelFlag() {
		PistolFlag = true;
	}

}
