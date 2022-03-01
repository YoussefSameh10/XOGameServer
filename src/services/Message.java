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

enum Action {
    Register,
    Login,
    GetOnlinePlayersList,
    Challenge,
    ForwardChallenge,
    DeclineChallenge,
    AcceptChallenge,
    RespondToChallenge,
    SendMove,
    ForwardMove,
    SendResult,
    Close,
    CloseServer
}

public class Message {
    Action action;
    String[] params;

    public Message() {
    }
}
