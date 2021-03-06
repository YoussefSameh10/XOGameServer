/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Youssef
 */
public class XOGameServer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        FXMLDocumentController controller = new FXMLDocumentController();
        
        Scene scene = new Scene(root);
        Image icon = new Image("assets/app-icon-xo.png");
        stage.getIcons().add(icon);
        stage.setTitle("Server Status");
        stage.setResizable(false);
        stage.setOnCloseRequest((event) -> {
            controller.didCloseServer();
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}