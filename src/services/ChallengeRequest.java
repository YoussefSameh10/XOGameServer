/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author dell
 */
public class ChallengeRequest implements ServerAction {
    String id1,id2;
    public ChallengeRequest(String id1,String id2){
      this.id1 = id1;
      this.id2 = id2;
      
    } 
    
    
}
