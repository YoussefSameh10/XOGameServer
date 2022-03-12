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
import models.Player;
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
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public boolean registerPlayer(String username, String password) {
        try {
            int id;
            ResultSet players = getRegisteredPlayers(); //3a4an ageb players last w azwd 3lih wa7d
            if (players.last()) {
                id = players.getInt("id") + 1;
            } else {
                id = 0; // lw mafi4 players ma3nah en el db fadya ezn id 0
            }

            PreparedStatement statement = con.prepareStatement("INSERT INTO PLAYERS VALUES (?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setInt(4, 0);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public ResultSet getRegisteredPlayers() throws SQLException {
        PreparedStatement getStmt = con.prepareStatement(
                "SELECT * FROM PLAYERS",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        ResultSet players = getStmt.executeQuery();
        return players;
    }

    @Override
    public Player loginPlayer(String username, String password) {
        ResultSet rs;
        System.out.println(username);
        System.out.println(password);
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM PLAYERS WHERE USERNAME=? AND PASSWORD=?");
            statement.setString(1, username);
            statement.setString(2, password);

            rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String userName = rs.getString(2);
                String myPassword = rs.getString(3);
                int score = rs.getInt(4);
                System.out.println(id);
                return new Player(id, userName, myPassword, score);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public int closePlayer(int id) {
        ResultSet rs;
        System.out.println("Client Closed " + id);

        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM PLAYERS WHERE ID=?");
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                int userid = rs.getInt(1);
                System.out.println("close func from db " + userid);
                return userid;
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public String getPlayerUsername(int ID) {
        ResultSet rs;
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM PLAYERS WHERE ID=?");
            statement.setInt(1, ID);
            rs = statement.executeQuery();
            if (rs.next()) {
                String username = rs.getString(2);
                return username;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return "";
    }

    @Override
    public int getPlayerScore(int ID) {
        ResultSet rs;
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM PLAYERS WHERE ID=?");
            statement.setInt(1, ID);
            rs = statement.executeQuery();
            if (rs.next()) {
                int playerScore = rs.getInt(4);
                return playerScore;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return -1;
    }

    @Override
    public int getNoOfPlayersInServer() {
        int cnt = 0;
        ResultSet rs;
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM PLAYERS");
            rs = statement.executeQuery();
            while (rs.next()) {
                cnt++;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
        return cnt;
    }

    public int IncreasePlayerScore(int ID) {
        try {
            int score = getPlayerScore(ID);
            if (score != -1) {
                PreparedStatement statement = con.prepareStatement("UPDATE PLAYERS SET SCORE=? WHERE ID=?");
                statement.setInt(1, score + 10);
                statement.setInt(2, ID);
                statement.executeUpdate();
                return 0;
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean saveGameMoves(int playerOneId, int playerTwoId, String gameMoves) {
        try {
            int id;
            ResultSet games = getSavedGames(); //3a4an ageb players last w azwd 3lih wa7d
            if (games.last()) {
                id = games.getInt("id") + 1;
            } else {
                id = 0; // lw mafi4 players ma3nah en el db fadya ezn id 0
            }

            PreparedStatement statement = con.prepareStatement("INSERT INTO GAME VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setInt(2, playerOneId);
            statement.setInt(3, playerTwoId);
            statement.setString(4, gameMoves);
            statement.setString(5, "f");
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public ResultSet getSavedGames() throws SQLException {
        PreparedStatement getStmt = con.prepareStatement(
                "SELECT * FROM GAME",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        ResultSet games = getStmt.executeQuery();
        return games;
    }

}
