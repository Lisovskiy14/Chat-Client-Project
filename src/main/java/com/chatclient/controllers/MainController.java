package com.chatclient.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.chatclient.business.SocketServiceThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea chatHistoryTA;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea writeTA;

    @FXML
    void onSendButtonClick(ActionEvent event) {
        try {
            SocketServiceThread.send(writeTA.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void refreshChatHistory(String sender, String message) {
        chatHistoryTA.appendText(sender + ": " + message + "\n");
    }

    @FXML
    void initialize() {
        SocketServiceThread.setController(this);
    }
}
