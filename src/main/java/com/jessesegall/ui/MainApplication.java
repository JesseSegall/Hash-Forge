package com.jessesegall.ui;

import com.jessesegall.blockchain.Blockchain;
import com.jessesegall.network.P2PNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainView.fxml")));
        primaryStage.setTitle("Hash Forge Wallet");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();

//        Blockchain blockchain = new Blockchain(7);
//        new Thread(() -> {
//            P2PNetwork network = new P2PNetwork(8080, blockchain);
//            network.start();
//        }).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
