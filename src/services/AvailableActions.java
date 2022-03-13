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
    Register("Register"), //DONE
    Login("Login"), //DONE
    GetOnlinePlayersList("GetOnlinePlayersList"),
    ChallengeRequest("ChallengeRequest"),
    ChallengeResponse("ChallengeResponse"),
    Move("Move"),
    GetMyGames("GetMyGames"),
    BackToOnline("BackToOnline"),
    Logout("Logout"),
    ClientClose("ClientClose");
    
    private String action;
 
    AvailableActions(String action) {
        this.action = action;
    }
 
    public String getString() {
        return action;
    }
}