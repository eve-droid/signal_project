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

public class WebSocketClientR extends WebSocketClient{
    DataStorage dataStorage;
    AlertManager alertManager;

    public WebSocketClientR(URI serverUri, DataStorage dataStorage, AlertManager alertManager) {
        super(serverUri);
        this.dataStorage = dataStorage;

        this.alertManager = alertManager;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connection to WebSocket server successful");
    }

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


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connections to WebSocket server closed because: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("An error occurred: " + ex.getMessage());
    }
    

}