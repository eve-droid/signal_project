package com.cardio_generator.outputs;

import java.net.URI;
import java.net.URISyntaxException;

import com.alerts.AlertManager;
import com.data_management.DataStorage;

public class InitializeWebClient {
    
    public InitializeWebClient(AlertManager alertManager, DataStorage dataStorage, int port) throws URISyntaxException {
        // URI for the client to connect to the server
            URI serverUri = new URI("ws://localhost:" + port);

            // Create and start the WebSocket client
            WebSocketClientR client = new WebSocketClientR(serverUri, dataStorage, alertManager);
            client.connect();
    }
}
