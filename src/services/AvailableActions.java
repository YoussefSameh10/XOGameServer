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
public enum AvailableActions {
    Register("Register"),
    Login("Login"),
    GetOnlinePlayersList("GetOnlinePlayersList"),
    Challenge("Challenge"),
    ForwardChallenge("ForwardChallenge"),
    DeclineChallenge("DeclineChallenge"),
    AcceptChallenge("AcceptChallenge"),
    RespondToChallenge("RespondToChallenge"),
    SendMove("SendMove"),
    ForwardMove("ForwardMove"),
    SendResult("SendResult"),
    Close("Close"),
    CloseServer("CloseServer");
    
    private String action;
 
    AvailableActions(String action) {
        this.action = action;
    }
 
    public String getString() {
        return action;
    } 
}