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
public interface Database {
    public boolean registerPlayer(String username, String password);
    public int loginPlayer(String username, String password);
}
