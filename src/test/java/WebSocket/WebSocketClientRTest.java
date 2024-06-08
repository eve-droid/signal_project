// WebSocketClientRTest.java
package WebSocket;

import org.java_websocket.handshake.ServerHandshake;
import org.junit.Before;
import org.junit.Test;

import com.alerts.AlertManager;
import com.cardio_generator.outputs.WebSocketClientR;
import com.data_management.DataStorage;

import java.net.URI;
import java.net.URISyntaxException;
import org.mockito.Mockito;

public class WebSocketClientRTest {

    private WebSocketClientR webSocketClientR;

    @Before
    public void setUp() throws URISyntaxException {
        DataStorage dataStorage = DataStorage.getInstance();
        AlertManager alertManager = new AlertManager();
        webSocketClientR = new WebSocketClientR(new URI("ws://localhost:8080"), dataStorage, alertManager);
    }
    @Test
    public void testOnOpen() {
        ServerHandshake serverHandshake = Mockito.mock(ServerHandshake.class); // Create a mock object of ServerHandshake
        webSocketClientR.onOpen(serverHandshake);
        // Verify that the connection message is printed
    }

    @Test
    public void testOnMessage() {
        String message = "Patient ID: 9, Timestamp: 1715250889771, Label: WhiteBloodCells, Data: 4.338229734876824";
        webSocketClientR.onMessage(message);
        webSocketClientR.onMessage("Invalid message");//check errors are handled
        // Verify that the message is parsed
    }

    @Test
    public void testOnClose() {
        int code = 666;
        String reason = "test closure";
        boolean remote = true;
        webSocketClientR.onClose(code, reason, remote);
        // Verify that the closure message is printed
    }

    @Test
    public void testOnError() {
        Exception e = new Exception("Test exception");
        webSocketClientR.onError(e);
        // Verify that the error message is printed
    }
}