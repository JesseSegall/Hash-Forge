package com.jessesegall.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class MainViewController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showOverview(); // Default view
    }

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

    private void loadView(String fxmlFile) {
        try {
            // Adjusted the path to include the 'fxml/' directory
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
