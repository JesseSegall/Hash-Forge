package com.jessesegall.ui;

import com.jessesegall.blockchain.*;
import com.jessesegall.network.P2PNetwork;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    private Blockchain blockchain;
    private UTXOManager utxoManager;
    private Wallet wallet;
    private TransactionPool transactionPool;
    private P2PNetwork p2pNetwork;
    private Miner miner;

    @FXML
    private StackPane contentArea;

    @FXML
    private Label balanceLabel;


    @FXML
    private void showOverview() {
        loadView("Overview.fxml");
    }

    @FXML
    private void showTransactions() {
        loadView("Transactions.fxml");
    }

    @FXML
    private void showSendReceive() {
        loadView("SendReceive.fxml");
    }

    @FXML
    private void showMine() {
        loadView("Mine.fxml");
    }


    @FXML
    public void initialize() {
        // Initialize the instances
        blockchain = new Blockchain(7);
        utxoManager = new UTXOManager();
        transactionPool = new TransactionPool();

        List<String> knownNodes = new ArrayList<>();
        knownNodes.add("localhost:8081");

        p2pNetwork = new P2PNetwork(8080, blockchain, knownNodes);

        wallet = new Wallet(blockchain, p2pNetwork);
        miner = new Miner(blockchain, utxoManager, wallet, p2pNetwork);

        // Start the P2PNetwork
        p2pNetwork.start();

        // Show the initial wallet balance
        updateWalletBalance();

        showOverview();
    }

    private void updateWalletBalance() {
        float balance = wallet.getBalance();
        //TODO: update the balance label
        balanceLabel.setText("Balance: " + balance);
    }

    private void loadView(String fxmlFile) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            //TODO logger
            e.printStackTrace();
            // Handle the exception, perhaps show an error message
        }
    }

    public void handleExit(ActionEvent actionEvent) {
    }

    public void handleSettings(ActionEvent actionEvent) {
    }

    public void handleAbout(ActionEvent actionEvent) {
    }
}
