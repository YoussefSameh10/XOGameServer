/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Youssef
 */
public class Game {
    private int ID;
    private int player1ID;
    private int player2ID;
    private String record;  
    private boolean player1Win;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(int player1ID) {
        this.player1ID = player1ID;
    }

    public int getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(int player2ID) {
        this.player2ID = player2ID;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public boolean isPlayer1Win() {
        return player1Win;
    }
    
    public void setPlayer1Win(boolean player1Win) {
        this.player1Win = player1Win;
    }

  
    public Game(int ID, int player1ID, int player2ID, String record, boolean player1Win) {
        this.ID = ID;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.record = record;
        this.player1Win = player1Win;
    }
}
