package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import com.alerts.AlertManager;
import com.data_management.DataStorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;
    private WebSocketClientR webSocketClientR;

    public WebSocketOutputStrategy(int port) throws URISyntaxException {
        InetSocketAddress address = new InetSocketAddress(port);
        server = new SimpleWebSocketServer(address);
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();


    }

    /*
     * assumes that the data is not an alert since the alerts are generated by the alert generator
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    
    public void sendDataFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming the line contains formatted data matching your client expectations
                for (WebSocket conn : server.getConnections()) {
                    conn.send(line);
                    System.out.println("Sent data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
