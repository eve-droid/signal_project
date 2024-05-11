package data_management;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataParser;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class AlertGeneratorTest {

    @Test
    public void conditionTesting() throws IOException{
        DataParser parser = new DataParser();
        DataStorage dataStorage = new DataStorage();
        parser.readData(dataStorage);
        parser.parseData("src/test/java/data_management/OutputFilesTest/testAlert.txt");

        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(new Patient(7));
        List <Alert> alertList = alertGenerator.getAlertsPatient(7);

        assertEquals(14, alertList.size());
    }
    

    /*public static void main (String[] args){
    
        try {
            File file = new File("src/test/java/data_management/OutputFilesTest/testAlert.txt");

            FileWriter writer = new FileWriter(file);

            writer.write("Patient ID: 7, Timestamp: 1715250889835, Label: DiastolicPressure, Data: 57.0\n");//too low
            writer.write("Patient ID: 7, Timestamp: 1715250889953, Label: DiastolicPressure, Data: 125.0\n");//too high

            writer.write("Patient ID: 7, Timestamp: 1715250890135, Label: SystolicPressure, Data: 189.0\n");//too high
            writer.write("Patient ID: 7, Timestamp: 1715250890035, Label: SystolicPressure, Data: 67.0\n");//too low

            writer.write("Patient ID: 7, Timestamp: 1715250890335, Label: DiastolicPressure, Data: 91.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890535, Label: DiastolicPressure, Data: 69.0\n");//decreasing trend
            writer.write("Patient ID: 7, Timestamp: 1715250890235, Label: DiastolicPressure, Data: 80.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890435, Label: DiastolicPressure, Data: 95.0\n");//increasing trend

            writer.write("Patient ID: 7, Timestamp: 1715250890235, Label: SystolicPressure, Data: 97.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890335, Label: SystolicPressure, Data: 120.0\n");//increasing trend
            writer.write("Patient ID: 7, Timestamp: 1715250890435, Label: SystolicPressure, Data: 108.0\n");
            writer.write("Patient ID: 7, Timestamp: 1715250890535, Label: SystolicPressure, Data: 91.0\n");//decreasing trend

            writer.write("Patient ID: 7, Timestamp: 1715250890635, Label: Saturation, Data: 90.0\n");//too low
            writer.write("Patient ID: 7, Timestamp: 1715250890735, Label: SystolicPressure, Data: 65.0\n");//combined with a sysPressure too low

            writer.write("Patient ID: 7, Timestamp: 1715250890835, Label: Saturation, Data: 84.0\n");//drop of 5% or more 

            writer.write("Patient ID: 7, Timestamp: 1715250890935, Label: ECG, Data: 0.035729735\n");
            writer.write("Patient ID: 7, Timestamp: 1715250891500, Label: ECG, Data: 0.309273\n");//too high
            writer.write("Patient ID: 7, Timestamp: 1715250892750, Label: ECG, Data: 0.408271\n");//too low

            // Close the writer and scanner
            writer.close();

            System.out.println("File created successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
        
    }*/
}
