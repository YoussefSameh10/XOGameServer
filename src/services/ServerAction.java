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

        
  class BackToOnline implements ServerAction {
    int secondPlayerID;

    public BackToOnline(int secondPlayerID) {
        this.secondPlayerID = secondPlayerID;
    }
    
}      
        
        
class GetOnlinePlayersList implements ServerAction {
    
}

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
    public ClientClose(){
        
    }
}


class GetMyGames implements ServerAction {
    int senderId;
    
    public GetMyGames(int senderId){
        this.senderId = senderId;
    
    }
}

class Logout implements ServerAction {
    
}