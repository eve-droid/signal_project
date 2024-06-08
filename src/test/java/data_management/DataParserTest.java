package data_management;

import static org.junit.Assert.assertEquals;


import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.data_management.DataParser;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;


/*
 * This class tests the DataParser class
 * it was modified to test the modified dataParser class
 */
public class DataParserTest {
    
    @Test
    public void parseData() throws IOException{

        DataParser parser = new DataParser();
        DataStorage dataStorage = DataStorage.getInstance();

        parser.readData(dataStorage, "Patient ID: 51, Timestamp: 1715250889818, Label: DiastolicPressure, Data: 72.0");
        parser.readData(dataStorage, "Patient ID: 39, Timestamp: 1715250889821, Label: DiastolicPressure, Data: 83.0");
        parser.readData(dataStorage, "Patient ID: 14, Timestamp: 1715250889700, Label: ECG, Data: 0.036438017312799664");
        parser.readData(dataStorage, "Patient ID: 69, Timestamp: 1715250889827, Label: DiastolicPressure, Data: 75.0");
        parser.readData(dataStorage, "Patient ID: 81, Timestamp: 1715250889678, Label: ECG, Data: -0.39235554144612894");
        parser.readData(dataStorage, "Patient ID: 84, Timestamp: 1715250889676, Label: Saturation, Data: 97.0");
        parser.readData(dataStorage, "Patient ID: 39, Timestamp: 1715250889750, Label: SystolicPressure, Data: 120.0");
        parser.readData(dataStorage, "Patient ID: 39, Timestamp: 1715250889750, Label: SystolicPressure, Data: 120.0");//shouldn't be added to data storage since duplicate record
        parser.readData(dataStorage, "Patient ID: 39, Timestamp: 1715250889750, Label: SystolicPressure, Data: low");//shouldn't be added to data storage since not a double
        

        List<PatientRecord> records = dataStorage.getRecords(51, 1700000000000L, 1800000000000L);

        assertEquals(1715250889818L, records.get(0).getTimestamp());

        assertEquals(72.0, (dataStorage.getRecord(51, 1715250889818L)).getMeasurementValue(), 0.0);//check DiastolicPressure

        assertEquals(0.036438017312799664, (dataStorage.getRecord(14, 1715250889700L)).getMeasurementValue(), 0.0);//check ECG positive value
        assertEquals(-0.39235554144612894, (dataStorage.getRecord(81, 1715250889678L)).getMeasurementValue(), 0.0);//check ECG negative value

        assertEquals(97.0, (dataStorage.getRecord(84, 1715250889676L)).getMeasurementValue(), 0.0);//check saturation

        assertEquals(120.0, (dataStorage.getRecord(39, 1715250889750L)).getMeasurementValue(), 0.0);//check SystolicPressure
    }

    /*@Test
    public void emptyAndWrongDirectoryTest() throws IOException{
        DataParser parser = new DataParser();
        DataStorage dataStorage = new DataStorage();

        parser.readData(dataStorage, "src/test/java/data_management/OutputFilesTest/EmptyDirectory"); //empty directory
        parser.readData(dataStorage, "src/test/java/data_management/OutputFilesTest/DoesNotExist"); //directory that doesn't exist
        parser.readData(dataStorage, "src/test/java/data_management/OutputFilesTest/wrongInputTest"); //data is not a double but is "low", so wrong input
        parser.readData(dataStorage, "src/test/java/data_management/OutputFilesTest/emptyFile"); //empty file
    }

    /*public static void main (String[] args){
        String [] names = {"DiastolicPressure", "Saturation", "SystolicPressure", "ECG"};
        int [] numberOfData = {5, 10, 20, 30};

        for(int i =0; i<names.length; i++){
            try {
                File file = new File("src/test/java/data_management/OutputFilesTest/test" + names[i] + ".txt");

                FileWriter writer = new FileWriter(file);

                File inputFile = new File("output/" + names[i] + ".txt");
                Scanner reader = new Scanner(inputFile);
                

                for(int j = 0; j < numberOfData[i] && reader.hasNextLine(); j++){
                    String line = reader.nextLine();
                    writer.write(line + "\n");
                }
                

                // Close the writer and scanner
                writer.close();
                reader.close();

                System.out.println("File created successfully!");
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file: " + e.getMessage());
            }
        }
    }*/

    
}
