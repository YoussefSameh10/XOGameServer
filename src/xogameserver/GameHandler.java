/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.RequestManager;
import services.ServerAction;

/**
 *
 * @author sandra
 */
public class GameHandler extends Thread {
    DataInputStream dis;
    PrintStream ps;
    public static Vector<GameHandler> onlineClients = new Vector<GameHandler>();
    static Vector<GameHandler> inGameClients = new Vector<GameHandler>();
    
    private int ID;
    
    RequestManager requestManager;
    
    public GameHandler(Socket cs) {
        requestManager = RequestManager.getInstance();
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            
            //when login successfully
            //GameHandler.onlineClients.add(this);
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void addOnlinePlayer(GameHandler onlinePlayer){
        onlineClients.add(onlinePlayer);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
    public static void addInGamePlayer(GameHandler inGamePlayer){
        inGameClients.add(inGamePlayer);
    }
    public void run() {
        while(true) {
            try {
                //y2ra el string w y7wlo l msg ll client
                String msg = dis.readLine();
                ServerAction action = requestManager.parse(msg);
                String response = requestManager.process(action, this);
                ps.println(response);
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }       
    
}