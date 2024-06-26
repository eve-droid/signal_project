package com.data_management;

import java.io.IOException;

public class DataParser implements DataReader{

    DataStorage dataStorage;
    
    /**
     * set the given data storage
     * 
     * @param dataStorage the data storage that will process the data read
     */
    public void readData(DataStorage dataStorage, String message) throws IOException{
        this.dataStorage = dataStorage;
        parseData(message);
    }

    /**
     * read and parse the data of the given file to then pass it to the Data storage for further processing (line by line)
     * 
     * @param output_dir path of the file to be read 
     */
    public void parseData(String message){

        try{
            String[] lineSplit = message.split(",");

            for(int i =0; i<lineSplit.length; i++){
                String [] onlyData = lineSplit[i].split(":");
                lineSplit[i] = onlyData[1];
            }

            //assumption: each line of the files has the patient number, the timestamp, the type of measurement (label) and the measurement value in this order separated by a comma
            int patientID = Integer.parseInt(lineSplit[0].trim());
            long timestamp = Long.parseLong(lineSplit[1].trim());
            String recordType = lineSplit[2].trim(); //e.g. ECG, blood pressure
            double data;
            if(recordType.equals("Saturation")){
                String[] dataString = lineSplit[3].split("%");
                data = Double.parseDouble(dataString[0].trim());
            } else{
                data = Double.parseDouble(lineSplit[3].trim()); //measurement value
            }
            

            dataStorage.addPatientData(patientID, data, recordType, timestamp); // pass the line read to the data storage
        } catch (Exception e){
            System.out.println("Message not in the correct format");
        }
            
        
    }
    

}
