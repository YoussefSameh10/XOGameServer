/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import models.Player;

/**
 *
 * @author Youssef
 */
public interface Database {
    public boolean registerPlayer(String username, String password);
    public Player loginPlayer(String username, String password);
    public int closePlayer(int id);
   // public String getPlayerAtID(int ID);
    public int getPlayerScore(int ID);
    public String getPlayerUsername(int ID);
    public int getNoOfPlayersInServer();
}
