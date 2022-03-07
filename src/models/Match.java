/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author amin
 */
public class Match {
       private int  playerOneId , playerTwoId ;
       private Board board  ;
       private boolean isGameStarter; 


    public Match(int playerOneId, int playerTwoId, Board board, boolean isGameStarter) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.board = board;
        this.isGameStarter = isGameStarter;
    }

    public int getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(int playerOneId) {
        this.playerOneId = playerOneId;
    }

    public int getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(int playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isIsGameStarter() {
        return isGameStarter;
    }

    public void setIsGameStarter(boolean isGameStarter) {
        this.isGameStarter = isGameStarter;
    }
    
}
