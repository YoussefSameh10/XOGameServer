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
import javafx.application.Platform;
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
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    private void stopServer(ActionEvent event) {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        System.out.println("stops");
        didCloseServer();
        Platform.exit();
        System.exit(0);
    }
    
    
    public void didCloseServer(){
        System.out.println("MSG FROM SERVER: URGENT SERVER CLOSE");
        for(GameHandler gh : GameHandler.onlineClients){
            System.out.println("MSG FROM SERVER: URGENT SERVER CLOSE  heloooooooo" + GameHandler.onlineClients.size());
            gh.ps.println("ServerClose,Success");
        }
        GameHandler.onlineClients.removeAllElements();
    }
}
