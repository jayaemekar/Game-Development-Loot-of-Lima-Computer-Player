package com.lol.computer.player;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.lol.constant.Constants;
import com.lol.helper.PlayerInformation;

public class QuestionLogic {
	static int count = 1;
	static int terrainCount1 = 100;
	static int terrainCount2 = 100;
	static String  Message = null;
	static String  Message2 = null;
	static int allTerrain1 = 0;
	static int allTerrain2 = 0;

	 public class Node{  
	        Map< String, Map< String , Integer >  > terrainList;  
	        String direction;
	        String number; 
	        Node next;  
	        public Node(Map< String, Map< String, Integer> > terrainList, String direction, String number) {  
	            this.terrainList = terrainList;  
	            this.direction = direction;
	            this.number = number;
	        }  
	    }  

	 	public static Node head = null;  
	    public Node tail = null;  
	  
	    //This function will add the new node at the end of the list.  
	    public void add(Map< String, Map<String, Integer > > terrainList, String direction, int number){  
	        //Create new node  
	        Node newNode = new Node(terrainList, direction, String.valueOf(number));  
	        //Checks if the list is empty.  
	        if(head == null) {  
	            head = newNode;  
	            tail = newNode;  
	            newNode.next = head;  
	        }  
	        else {  
	            tail.next = newNode;  
	            tail = newNode;  
	            tail.next = head;  
	        }  
	    }  
	    
	    public void display() {  
	        Node current = head;  
	        if(head == null) {  
	            System.out.println("List is empty");  
	        }  
	        else {  
	            System.out.println("Nodes of the circular linked list: ");  
	             do{  

	                System.out.print(" "+ current.terrainList);  
	                System.out.print(" "+ current.direction);  
	                System.out.print(" "+ current.number);  

	                System.out.print("\n");  
	                current = current.next;  
	            }while(current != head);  
	            System.out.println();  
	        }  
	    }  
	  
	    
	    
	    public static void initTerrainTokens( PlayerInformation PlayerInfo) {
	    	
	    	int noPlayer =PlayerInfo.getNumberOfPlayers(); 
	    	QuestionLogic cp = new QuestionLogic();
	    	Map< String, Integer  > playerList = new HashMap<>();

        for (int no = 1; no <= noPlayer; no++) {
        			String playerName = "P" + String.valueOf(no);
        			
        			playerList.put(playerName, -1);
        	} 
        List<String> location = new ArrayList<String>();
        location.add(Constants.NORTH_CHAR);
        location.add(Constants.NORTH_EAST_CHAR);
        location.add(Constants.NORTH_WEST_CHAR);
        location.add(Constants.SOUTH_CHAR);
        location.add(Constants.SOUTH_EAST_CHAR);
        location.add(Constants.SOUTH_WEST_CHAR);
        location.add(Constants.WEST_CHAR);
        location.add(Constants.EAST_CHAR);

        	
	        location.stream().forEach( loc -> {
	        
	        	Map< String, Map< String, Integer > >terrainList = new HashMap<>();
		        terrainList.put(Constants.MOUNTAINS_CHAR, playerList);
		        terrainList.put(Constants.BEACH_CHAR, playerList);
				terrainList.put(Constants.FOREST_CHAR, playerList);
				cp.add(terrainList, loc, count);
				count ++; 
	        });
	        
	       cp.display();
        	
       }
	    
	    
	    
//	    ,2M,2F,3B,4F,7M,7B,7F
	    
	  public static void addPersonalToken( List<String> messageDetailsList, String playerName ) {
		  
		  System.out.println("in personal");
		  
		  QuestionLogic cp1 = new QuestionLogic();

		  
		  messageDetailsList.stream().forEach(token ->{
			  
			  
			  String dir = String.valueOf(token.charAt(0));
			  String terrianToken = String.valueOf(token.charAt(1));
			  Node current = head;
			  if(head == null) {  
				  	return;
			  } 
			  else {  
				  
				  do{  
					  System.out.println(current.number);
					  if(current.number.equals(dir) ) {
						
						  current.terrainList.entrySet().stream().filter(terrian -> terrian.getKey().equals(terrianToken))
						   .forEach(
									terrianMap -> {
										HashMap<String, Integer> map = new HashMap<>();

											terrianMap.getValue().entrySet().stream().forEach(playerMap -> {
														if(playerMap.getKey().equals(playerName)) {
															map.put(playerMap.getKey(), 1);
														} 
														else
															map.put(playerMap.getKey(), 0);
														
												});
											terrianMap.setValue(map);
										}
									);
						  	break;
						

						  
					  } 
					  	current = current.next;  
		            }while(current != head); 
				
		        }  
		  });
		  		 
}

//		 * SEM,NWB,SWF

	public static void createQuestion(List<String> messageDetailsList, String playerName) {
		
		HashMap<String, HashMap<String, Integer>> map1 = new HashMap<>();
		HashMap<String, HashMap<String, Integer>> map2 = new HashMap<>();

		


		String headToken = messageDetailsList.get(0);
		String headDir = String.valueOf(headToken.charAt(0)) + String.valueOf(headToken.charAt(1));
		String headTerrian = String.valueOf(headToken.charAt(2));
		
		String tailToken = messageDetailsList.get(1);
		String tailDir = String.valueOf(tailToken.charAt(0)) + String.valueOf(tailToken.charAt(1));
		System.out.println(tailToken +" " +tailDir );
		String tailTerrian = String.valueOf(tailToken.charAt(2));
		
		String tail2Token = messageDetailsList.get(2);
		String tail2Dir = String.valueOf(tail2Token.charAt(0)) + String.valueOf(tail2Token.charAt(1));
		String tail2Terrian = String.valueOf(tail2Token.charAt(2));
		
		Node temp = head;  
		Node tail1 = null;
		Node tail2 = null;

        
		        do{  
		       	 
		       	 	if(temp.direction.equals(headDir) ) {    
		       			head = temp;
		             
		       	 	} 
		       	 	if(temp.direction.equals(tailDir) ) {    
		       			tail1 = temp;
		             
		       	 	}
		       	 	if(temp.direction.equals(tail2Dir) ) {    
		       			tail2 = temp;
		             
		       	 	}
		       	 	temp = temp.next;
		       	 	
		       }while(temp != head); 
        
  
	   Node current = head;
	   System.out.println(current.direction);
	
	   do{  
			System.out.println(current.direction);

					  current.terrainList.entrySet().stream()
					   .forEach(
							   
								terrianMap -> {
										System.out.println(terrianMap.getKey());		
										terrianMap.getValue().entrySet().stream().forEach(playerMap -> {
													if(!playerMap.getKey().equals(playerName) && playerMap.getValue() == -1) {
														
															if(!map1.containsKey(playerMap.getKey())) {
																map1.put(playerMap.getKey(), new HashMap<String, Integer>());
															}
															if (!map1.get(playerMap.getKey()).containsKey(terrianMap.getKey())) {
																map1.get(playerMap.getKey()).put(terrianMap.getKey(), 0);
															}
															int count=0;
															count = map1.get(playerMap.getKey()).get(terrianMap.getKey());
															count = count +1;
															map1.get(playerMap.getKey()).get(terrianMap.getKey());
															map1.get(playerMap.getKey()).replace(terrianMap.getKey(), count);
													} 
													
													
											});
									}
								
								);
				
				current = current.next;  
				  
	            }while(current != tail1); 
	   
	   
	   map1.entrySet().forEach( player->{
		   
		   player.getValue().entrySet().stream().forEach( terrain ->{
			   
			   int terrCount = 0;
			   terrCount = terrain.getValue();
			   if(terrCount <= terrainCount1) {
				   
				   Message = player.getKey() + "," +terrain + "," + headToken + "," +  tailToken;
				   terrainCount1 = terrCount;
				   
			   }
			   
		   });
		   
	   });
	   
	   
	   Node current2 = head;
	   
	   do{  
				
				  current2.terrainList.entrySet().stream()
				   .forEach(
							terrianMap -> {

									terrianMap.getValue().entrySet().stream().forEach(playerMap -> {
												if(!playerMap.getKey().equals(playerName) && playerMap.getValue() == -1) {

													if(!map2.containsKey(playerMap.getKey())) {
														map2.put(playerMap.getKey(), new HashMap<String, Integer>());
													}
													if (!map2.get(playerMap.getKey()).containsKey(terrianMap.getKey())) {
														map2.get(playerMap.getKey()).put(terrianMap.getKey(), 0);
													}
													int count=0;
													count = map2.get(playerMap.getKey()).get(terrianMap.getKey());
													count = count +1;
													map2.get(playerMap.getKey()).get(terrianMap.getKey());
													map2.get(playerMap.getKey()).replace(terrianMap.getKey(), count);
												} 
												
												
										});
								}
							);
			
			  	current2 = current2.next;  
         }while(current2 != tail2); 
	   
	   map2.entrySet().forEach( player->{
		   
		   player.getValue().entrySet().stream().forEach( terrain ->{
			   
			   int terrCount = 0;
			   terrCount = terrain.getValue();
			   if(terrCount <= terrainCount2) {
				   terrainCount2 = terrCount;

				   Message2 = player.getKey() + "," +terrain + "," + headToken + "," +  tail2Token;
				   
			   }
			   
		   });
		   
	   });
	   
	   System.out.println(Message);
	   
	   System.out.println(Message2);
	   
			  
			  
	}



}
