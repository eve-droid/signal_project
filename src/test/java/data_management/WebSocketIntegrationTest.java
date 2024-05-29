package data_management;

import com.alerts.Alert;
import com.alerts.AlertManager;
import com.cardio_generator.outputs.InitializeWebClient;
import com.cardio_generator.outputs.WebSocketClientR;
import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WebSocketIntegrationTest {


    @Test
    public void testIntegration() throws URISyntaxException {
        DataStorage dataStorage = new DataStorage();
        AlertManager alertManager = new AlertManager(); 
        WebSocketClientR webSocketClientR = new WebSocketClientR(new URI("ws://localhost:8080"), dataStorage, alertManager);

        String message = "Patient ID: 79, Timestamp: 1715250889771, Label: DiastolicPressure, Data: 57";

        // Simulate receiving a message
        webSocketClientR.onMessage(message);

        // Verify that the message is correctly stored
        PatientRecord record = dataStorage.getRecord(79, 1715250889771L);
        String recordString = String.format("Patient ID: %d, Timestamp: %d, Label: %s, Data: %d", record.getPatientId(), record.getTimestamp(), record.getRecordType(), Math.round(record.getMeasurementValue()));
        assertEquals(message, recordString);

        alertManager.evaluateData(new Patient(79), dataStorage);

        // Verify that the alert is correctly generated
        List<Alert> alertList = alertManager.getAlertsPatient(79);
        assertEquals("Critical Treshold Alert: Diastolic Pressure to low", alertList.get(0).getCondition()); // Assuming this method exists
    }

    @Test
    public void testAlertGeneratorWithServerAndClient() throws IOException, URISyntaxException{
        AlertManager alertManager = new AlertManager(); 
        DataStorage dataStorage = new DataStorage();
        WebSocketOutputStrategy server = new WebSocketOutputStrategy(9090);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        InitializeWebClient client = new InitializeWebClient(alertManager, dataStorage, 9090);

        try {
            Thread.sleep(2000);
            server.sendDataFromFile("src/test/java/data_management/OutputFilesTest/testAlert.txt");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List <Alert> alertList = alertManager.getAlertsPatient(7);

        assertEquals(14, alertList.size());
    }
    

    /*public static void main (String[] args){
    
        try {
            File file = new File("src/test/java/data_management/OutputFilesTest/testAlert.txt");

            FileWriter writer = new FileWriter(file);

            writer.write("Patient ID: 7, Timestamp: 1715250889835, Label: DiastolicPressure, Data: 57.0\n");//too low alert
            writer.write("Patient ID: 7, Timestamp: 1715250889953, Label: DiastolicPressure, Data: 125.0\n");//too high alert

            writer.write("Patient ID: 7, Timestamp: 1715250890135, Label: SystolicPressure, Data: 189.0\n");//too high alert
            writer.write("Patient ID: 7, Timestamp: 1715250890035, Label: SystolicPressure, Data: 67.0\n");//too low alert

            writer.write("Patient ID: 7, Timestamp: 1715250890335, Label: DiastolicPressure, Data: 91.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890535, Label: DiastolicPressure, Data: 69.0\n");//decreasing trend alert
            writer.write("Patient ID: 7, Timestamp: 1715250890235, Label: DiastolicPressure, Data: 80.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890435, Label: DiastolicPressure, Data: 95.0\n");//increasing trend alert

            writer.write("Patient ID: 7, Timestamp: 1715250890235, Label: SystolicPressure, Data: 97.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890335, Label: SystolicPressure, Data: 120.0\n");//increasing trend alert
            writer.write("Patient ID: 7, Timestamp: 1715250890435, Label: SystolicPressure, Data: 108.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890535, Label: SystolicPressure, Data: 91.0\n");//decreasing trend alert

            writer.write("Patient ID: 7, Timestamp: 1715250890635, Label: Saturation, Data: 90.0\n");//too low alert
            writer.write("Patient ID: 7, Timestamp: 1715250890735, Label: SystolicPressure, Data: 65.0\n");//combined with a sysPressure too low alert

            writer.write("Patient ID: 7, Timestamp: 1715250890835, Label: Saturation, Data: 84.0\n");//drop of 5% or more alert 

            writer.write("Patient ID: 7, Timestamp: 1715250890935, Label: ECG, Data: 0.035729735\n");
            writer.write("Patient ID: 7, Timestamp: 1715250891500, Label: ECG, Data: 0.309273\n");//106 bpm, too high alert
            writer.write("Patient ID: 7, Timestamp: 1715250892750, Label: ECG, Data: 0.408271\n");//48 bpm, too low alert
            writer.write("Patient ID: 7, Timestamp: 1715250893750, Label: ECG, Data: 0.408271\n");//60 bpm
            writer.write("Patient ID: 7, Timestamp: 1715250894430, Label: ECG, Data: 0.408271\n");//88 bpm
            writer.write("Patient ID: 7, Timestamp: 1715250895100, Label: ECG, Data: 0.408271\n");//90bpm
            writer.write("Patient ID: 7, Timestamp: 1715250895930, Label: ECG, Data: 0.408271\n");//72 bpm
            writer.write("Patient ID: 7, Timestamp: 1715250896600, Label: ECG, Data: 0.408271\n");//90 bpm
            writer.write("Patient ID: 7, Timestamp: 1715250897600, Label: ECG, Data: 0.408271\n");//60 bpm, irregular bpm alert

            writer.write("Patient ID: 7, Timestamp: 1715251590835, Label: Saturation, Data: 75.0\n");//drop of 5% or more but not in a ten min window so only too low alert
            writer.write("Patient ID: 7, Timestamp: 1715251590845, Label: Saturation, Data: 94.0\n");//normal saturation

            // Close the writer and scanner
            writer.close();

            System.out.println("File created successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
        
    }*/
}