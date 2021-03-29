package com.lol.computer.player;

import java.util.HashMap;
import java.util.List;

import com.lol.helper.PlayerInformation;

public class QuestionDeductionLogic {
	
	 static int terrainCount1=100;
	 static String Message;
	
	
	public static void createQuestion(List<String> messageDetailsList, String playerName) {
		
	    
	    
	    
	    
	    
	    String headToken = messageDetailsList.get(0);
	   
	    
	    String tailToken = messageDetailsList.get(1);
	   
	    
	    String tail2Token = messageDetailsList.get(2);
	    
	    createQuestion(headToken,tailToken, tail2Token);
	    createQuestion(tailToken,headToken, tail2Token);
	    createQuestion(tail2Token,tailToken, headToken);
	    
	}
	
	public static String createQuestion( String headToken, String tailToken, String tail2Token) {
		
			String headDir = String.valueOf(headToken.charAt(0)) + String.valueOf(headToken.charAt(1));
		    String headTerrian = String.valueOf(headToken.charAt(2));
		    
		    String tailDir = String.valueOf(tailToken.charAt(0)) + String.valueOf(tailToken.charAt(1));
		    System.out.println(tailToken +" " +tailDir );
		    String tailTerrian = String.valueOf(tailToken.charAt(2));
		    
		    String tail2Dir = String.valueOf(tail2Token.charAt(0)) + String.valueOf(tail2Token.charAt(1));
		    String tail2Terrian = String.valueOf(tail2Token.charAt(2));
		
	    HashMap<String, HashMap<String, Integer>> map1 = new HashMap<>();
	    Node temp = ComputerPlayer.getInstance().getHead();
 	    Node tail1 = null;
 	    Node tail2 = null;

		
	    do{
	    	
	    	 	
	    	
	        if(temp.direction.equals(headDir) ) {
	        	ComputerPlayer.getInstance().setHead(temp); 
	            
	        }
	        if(temp.direction.equals(tailDir) ) {
	            tail1 = temp;
	            
	        }
	        if(temp.direction.equals(tail2Dir) ) {
	            tail2 = temp;
	            
	        }
	        temp = temp.next;
	        
	    }while(temp != ComputerPlayer.getInstance().getHead());
	    
	    
	    Node current = ComputerPlayer.getInstance().getHead();
	    System.out.println(current.direction);
	    
	    do{
	        System.out.println(current.direction);
	        
	        current.terrainList.entrySet().stream()
	            .forEach(
	                
	                terrianMap -> {
	                	
	                    System.out.println(terrianMap.getKey());
	                    
	                    terrianMap.getValue().entrySet().stream().forEach(playerMap -> {
	                    	
	                        if(!playerMap.getKey().equals(PlayerInformation.getInstance().getPlayerName()) && playerMap.getValue() == -1) {
	                            
	                            if(!map1.containsKey(playerMap.getKey())) {
	                                map1.put(playerMap.getKey(), new HashMap<String, Integer>());
	                            }
	                            
	                            
	                            if (!map1.get(playerMap.getKey()).containsKey(terrianMap.getKey().substring(1,2)) ) {
	                            	
	                                map1.get(playerMap.getKey()).put( terrianMap.getKey().substring(1,2), 0 );
	                            }
	                            int count=0;
	                            count = map1.get(playerMap.getKey()).get(terrianMap.getKey().substring(1, 2));
	                            count = count +1;
	                            
	                            map1.get(playerMap.getKey()).replace(terrianMap.getKey().substring(1, 2), count);
	                            
	                        }
	                        
	                        
	                        });
	                }
	                
	        );
	        
	        current = current.next;
	        
	    }while(current != tail1);
	    
	    //SW M SE F
	    //map1 { {P2: {B, 1} , {2B, 2} , {M, 1} } , {P3: {F,5} } } }
	   
	    System.out.println(map1);
	    
		map1.entrySet().forEach( player -> {
	    
		    player.getValue().entrySet().stream().forEach( terrain ->{
		   
			if(terrain.getValue() <= terrainCount1) {
		    
		    Message = player.getKey() + "," +terrain.getKey() + "," + headToken + "," +  tailToken;
		    
		    terrainCount1 = terrain.getValue();
	    
	    }
	    
	    });
	    
	    });
	    
	    System.out.println(Message);

	    
	    
	    return Message;
	}






}
