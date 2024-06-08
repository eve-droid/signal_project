package com.cardio_generator.outputs;

import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.alerts.AlertManager;
import com.data_management.DataParser;
import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * Represents a WebSocket client that receives data from a WebSocket server
 */
public class WebSocketClientR extends WebSocketClient{
    DataStorage dataStorage;
    AlertManager alertManager;

    public WebSocketClientR(URI serverUri, DataStorage dataStorage, AlertManager alertManager) {
        super(serverUri);
        this.dataStorage = dataStorage;

        this.alertManager = alertManager;
    }

    /**
     * Connects to the WebSocket server
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connection to WebSocket server successful");
    }

    /**
     * Receives a message from the WebSocket server, parses the data, stores it, and evaluates it for alerts
     */
    @Override
    public void onMessage(String message) {
        DataParser parser = new DataParser();
        try {
            System.out.println("Received message: " + message);
            parser.readData(dataStorage, message);
            System.out.println("Data parsed and stored successfully");

            Patient patient = dataStorage.getPatient(message);
 
            alertManager.evaluateData(patient, dataStorage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the WebSocket server
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connections to WebSocket server closed because: " + reason);
    }

    /**
     * Handles errors that occur during the connection to the WebSocket server
     */
    @Override
    public void onError(Exception ex) {
        System.out.println("An error occurred: " + ex.getMessage());
    }
    

}
