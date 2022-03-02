/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Youssef
 */



public class DAO implements Database {
    private static DAO instance = null;
    private Connection con;
    
    private DAO() {
        
        try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/XOGame", "root", "root");
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Database getInstance() {
        if(instance == null){
           instance =  new DAO(); 
        }
        return  instance;
    }
    
    public boolean registerPlayer(String username, String password) {
        try {
            int id;
            ResultSet players = getRegisteredPlayers();
            if(players.last()) {
                id = players.getInt("id") + 1;
            }
            else {
                id = 0;
            }
            
            PreparedStatement statement = con.prepareStatement("INSERT INTO PLAYER VALUES (?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setInt(4, 0);
            statement.executeUpdate();
            return true;         
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public ResultSet getRegisteredPlayers() throws SQLException {
        PreparedStatement getStmt = con.prepareStatement(
                "SELECT * FROM PLAYER",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        ResultSet players = getStmt.executeQuery();
        return players;
    }
}
