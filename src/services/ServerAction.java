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

class Challenge implements ServerAction {
    
}

class ForwardChallenge implements ServerAction {
    
}

class DeclineChallenge implements ServerAction {
    
}

class AcceptChallenge implements ServerAction {
    
}

class RespondToChallenge implements ServerAction {
    
}

class SendMove implements ServerAction {
    
}

class ForwardMove implements ServerAction {
    
}

class SendResult implements ServerAction {
    
}

class Close implements ServerAction {
    
}

class CloseServer implements ServerAction {
    
}