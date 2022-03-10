/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author Youssef
 */

public interface ServerAction {
    
}

class Register implements ServerAction {
    String username;
    String password;

    public Register(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Login implements ServerAction {
    String userName;
    String password;

    public Login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
}

class GetOnlinePlayersList implements ServerAction {
    
}

//class ChallengeRequest implements ServerAction {
//    String id1,id2;
//    public ChallengeRequest(String id1,String id2){
//      this.id1 = id1;
//      this.id2 = id2;
//    } 
//    
//}

//class ChallengeResponse implements ServerAction {
//    String respons;
//    public ChallengeResponse(String respons){
//        this.respons = respons;
//    } 
//    
//}


class Move implements ServerAction {
    int cellNumber ;
    int senderId;
    int recieverId;
    
    public Move(int cellNumber , int senderId , int recieverId){
        this.cellNumber = cellNumber;
        this.senderId     = senderId ;
        this.recieverId    = recieverId;
    }
}

class ClientClose implements ServerAction {
    int userID;
   
    public ClientClose(int userID) {
        this.userID = userID;
    }
}