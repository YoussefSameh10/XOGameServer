/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import services.ServerAction;
import services.RequestManager;

/**
 *
 * @author Youssef
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    
    ServerSocket serverSocket;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void startServer(ActionEvent event) {
        System.out.println("start");
        // Running Server
        try {
            serverSocket = new ServerSocket(8080);
            new Thread() {
                public void run() {
                   while(true) {
                       try {
                           Socket s = serverSocket.accept();
                           new GameHandler(s);
                       } catch (IOException ex) {
                           Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    } 
                }
            }.start();
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void stopServer(ActionEvent event) {
        System.out.println("stop");
    }
}


class GameHandler extends Thread {
    DataInputStream dis;
    PrintStream ps;
    static Vector<GameHandler> onlineClients = new Vector<GameHandler>();
    static Vector<GameHandler> inGameClients = new Vector<GameHandler>();
    
    int ID;
    
    RequestManager requestManager;
    
    public GameHandler(Socket cs) {
        requestManager = RequestManager.getInstance();
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            
            //GameHandler.onlineClients.add(this);
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run() {
        while(true) {
            try {
                String msg = dis.readLine();
                ServerAction action = requestManager.parse(msg);
                String response = requestManager.process(action);
                ps.println(response);
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
            
    
}
