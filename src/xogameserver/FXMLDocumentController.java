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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import services.DAO;
import services.Database;
import services.ServerAction;
import services.RequestManager;

/**
 *
 * @author Youssef
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private ImageView startButton;
    @FXML
    private ImageView stopButton;
    
    ServerSocket serverSocket;
    @FXML
    private PieChart playersPieChart;
    @FXML
    private ImageView refreshButton;
    
    private int noOfPlayersInServer;
    private int noOfPlayersInGame;
    private int noOfOnlinePlayers;
    private int noOfOfflinePlayers;
    private final Database db = DAO.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshData();
    }
    
    
    public void didCloseServer(){
        System.out.println("MSG FROM SERVER: URGENT SERVER CLOSE");
        for(GameHandler gh : GameHandler.onlineClients){
            System.out.println("MSG FROM SERVER: URGENT SERVER CLOSE  heloooooooo" + GameHandler.onlineClients.size());
            gh.ps.println("ServerClose,Success");
        }
        for(GameHandler gh : GameHandler.inGameClients){
            System.out.println("MSG FROM SERVER: URGENT SERVER CLOSE  heloooooooo" + GameHandler.inGameClients.size());
            gh.ps.println("ServerClose,Success");
        }
        
        GameHandler.onlineClients.removeAllElements();
        GameHandler.inGameClients.removeAllElements();
    }

    @FXML
    private void refreshPieChart(MouseEvent event) {
        refreshData();
    }

    @FXML
    private void startServer(MouseEvent event) {
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
    private void stopServer(MouseEvent event) {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        System.out.println("stops");
        didCloseServer();
        Platform.exit();
        System.exit(0);
    }
    
    private void refreshData()
    {
        noOfPlayersInServer = db.getNoOfPlayersInServer();
        noOfPlayersInGame   = GameHandler.inGameClients.size();
        noOfOnlinePlayers   = GameHandler.onlineClients.size();
        noOfOfflinePlayers  = noOfPlayersInServer - (noOfOnlinePlayers + noOfPlayersInGame);
        
        ObservableList<Data> list = FXCollections.observableArrayList(
                new PieChart.Data("Online", noOfOnlinePlayers),
                new PieChart.Data("Offline", noOfOfflinePlayers),
                new PieChart.Data("In-Game", noOfPlayersInGame)
        );
        
        playersPieChart.setData(list);
    }
}
