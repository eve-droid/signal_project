package com.cardio_generator.outputs;

import java.net.URI;
import java.net.URISyntaxException;

import com.alerts.AlertManager;
import com.data_management.DataStorage;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        // Start the WebSocket server on port 8080
        int port = 8080;
        WebSocketOutputStrategy server = new WebSocketOutputStrategy(port);

        // Wait a bit to be sure the server started
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataStorage dataStorage = new DataStorage();
        AlertManager alertManager = new AlertManager();

        // Initialize the WebSocket client
        InitializeWebClient client = new InitializeWebClient(alertManager, dataStorage, port);

            // Simulate server output to test the connection
            try {
                Thread.sleep(2000);
                server.sendDataFromFile("src/test/java/data_management/OutputFilesTest/testAlert.txt");
                
                //Thread.sleep(2000);//wait for the client to receive the message
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dataStorage.printAllPatients();
            alertManager.printAllAlerts();
            System.out.println(alertManager.getAlertsPatient(7).size());

    }
}