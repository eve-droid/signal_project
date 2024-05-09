package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataParser implements DataReader{

    DataStorage dataStorage;
    
    /**
     * set the given data storage
     * 
     * @param dataStorage the data storage that will process the data read
     */
    public void readData(DataStorage dataStorage) throws IOException{
        this.dataStorage = dataStorage;
    }

    /**
     * read and parse the data of the given file to then pass it to the Data storage for further processing (line by line)
     * 
     * @param output_dir path of the file to be read 
     */
    public void parseData(String output_dir){

        try{
            File file = new File(output_dir);

            if(file != null){

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] lineSplit = line.split(",");

                        //assumption: each line of the files has the patient number, the timestamp, the type of measurement (label) and the measurement value in this order separated by a comma
                        int patientID = Integer.parseInt(lineSplit[0]);
                        long timestamp = Long.parseLong(lineSplit[1]);
                        String recordType = lineSplit[2]; //e.g. ECG, blood pressure
                        double data = Double.parseDouble(lineSplit[3]); //measurement value

                        dataStorage.addPatientData(patientID, data, recordType, timestamp); // pass the line read to the data storage
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    

}
