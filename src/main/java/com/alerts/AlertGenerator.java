package com.alerts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.cardio_generator.outputs.OutputStrategy;
import com.cardio_generator.outputs.TcpOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private AlertStorage alertStorage = new AlertStorage();
    private AlertManager alertManager = new AlertManager(alertStorage);

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    
    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {

        String patientId = patient.getPatientId();
        List<PatientRecord> patientRecord = dataStorage.getRecords(Integer.parseInt(patientId), 0, 2000000000000L); //not sure of the endtime   
        System.out.println("test");
        boolean decreaseInDP = false; //keeps track if the last two diastolic measurements showed a decrease
        boolean increaseInDP = false; //keeps track if the last two diastolic measurements showed an increase
        double lastDiastolicPressure = -1; //keeps track of the last diastolic measurement
        boolean decreaseInSP = false; //keeps track if the last two systolic measurements showed a decrease
        boolean increaseInSP = false; //keeps track if the last two systolic measurements showed an increase
        double lastSystolicPressure = -1; //keeps track of the last systolic measurement
        boolean systolicPressureTooLow = false; //keeps track if the last systolic measurement was too low
        boolean saturationTooLow = false; //keeps track if the last saturation measurement was too low
        int irregularBpm = 0; //keeps track of the number of consecutive irregular bpm
        double lastBpm = -1; //keeps track of the last bpm measurement 

        for (int i = 0; i < patientRecord.size(); i++){

            PatientRecord record = patientRecord.get(i);
            double measurement = record.getMeasurementValue();

            switch (record.getRecordType()){

                case "DiastolicPressure": 

                    //Treshold check
                    if (measurement < 60){
                        triggerAlert(new Alert(patientId, "Critical Treshold Alert: Diastolic Pressure to low", record.getTimestamp()));
                    } else if(measurement > 120){
                        triggerAlert(new Alert(patientId, "Critical Treshold Alert: Diastolic Pressure to high", record.getTimestamp()));
                    }

                    //verify if there is a decrease/increase in the measurements
                    //if there already was a decrease/increase reported, then it is considered a trend and an alert is triggered
                    if((measurement < lastDiastolicPressure -10) && lastDiastolicPressure > 0){
                        if(!decreaseInDP){
                            decreaseInDP = true;
                            increaseInDP = false;
                        } else {
                            triggerAlert(new Alert(patientId, "Decreasing Trend Alert", record.getTimestamp()));
                        }
                    }
                    else if ((measurement > lastDiastolicPressure +10) && lastDiastolicPressure > 0){
                        if(!increaseInDP){
                            increaseInDP = true;
                            decreaseInDP = false;
                        } else {
                            triggerAlert(new Alert(patientId, "Increasing Trend Alert", record.getTimestamp()));
                        }
                    } else{
                        increaseInDP = false;
                        decreaseInDP = false;
                    }
                    lastDiastolicPressure = measurement;
                    
                    break;

                case "SystolicPressure" : 
                    systolicPressureTooLow = false;

                    //Treshold check
                    if (measurement < 90){
                        if(saturationTooLow){//if a low saturation is combined with a low systolic pressure, it's a hypotensive hypoxemia alert
                            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Hypotensive Hypoxemia Alert", record.getTimestamp()));
                        } else {
                            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Systolic pressure too low", record.getTimestamp()));
                        }

                        systolicPressureTooLow = true;
                    } else if(measurement > 180){
                        triggerAlert(new Alert(patientId, "Critical Treshold Alert: Systolic pressure too high", record.getTimestamp()));
                    }

                    //verify if there is a decrease/increase in the measurements
                    //if there already was a decrease/increase reported, then it is considered a trend and an alert is triggered
                    if(measurement < lastSystolicPressure -10){
                        if(!decreaseInSP){
                            decreaseInSP = true;
                            increaseInSP = false;
                        } else {
                            triggerAlert(new Alert(patientId, "Decreasing Trend Alert", record.getTimestamp()));
                        }
                    }
                    else if (measurement > lastSystolicPressure +10){
                        if(!increaseInSP){
                            increaseInSP = true;
                            decreaseInSP = false;
                        } else {
                            triggerAlert(new Alert(patientId, "Increasing Trend Alert", record.getTimestamp()));
                        }
                    } else{
                        increaseInSP = false;
                        decreaseInSP = false;
                    }
                    lastSystolicPressure = measurement;
                
                    break;

                case "Saturation":
                    saturationTooLow = false;

                    //Treshold check
                    if(measurement < 92){
                        if(systolicPressureTooLow){//if a low saturation is combined with a low systolic pressure, it's a hypotensive hypoxemia alert
                            triggerAlert(new Alert(patientId, "Hypotensive Hypoxemia Alert", record.getTimestamp()));
                        } else{
                            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Blood Saturation too low", record.getTimestamp()));
                        }
                        saturationTooLow = true;
                    }

                    long timestamp = record.getTimestamp();
                    PatientRecord patiantData = record;
                    int t;

                    //check if the saturation dropped by 5% or more in a window of 10 minutes before the measurement
                    for(t = i-1; t < patientRecord.size() && patiantData.getTimestamp() <= timestamp - (10*60*1000); t--){
                        patiantData = patientRecord.get(t);
                        if((patiantData.getRecordType().equals("Saturation")) && (patiantData.getMeasurementValue() <= measurement +5)){
                            triggerAlert(new Alert(patientId, "Decreasing Trend Alert", patiantData.getTimestamp()));
                            break;
                        }
    
                    }
                    break;

                case "ECG":

                    //find the next ECG measurement to compute the bpm (60/(RR-intervals))
                    for(int k = i+1; k < patientRecord.size(); k++){
                        if(patientRecord.get(k).getRecordType().equals("ECG")){
                            double bpm = 60/Math.abs(record.getTimestamp()-patientRecord.get(k).getTimestamp());

                            //Treshold check
                            if (bpm < 50){
                                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Heart Rate to low", record.getTimestamp()));
                            } else if(bpm > 100){
                                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Heart Rate to high", record.getTimestamp()));
                            }

                            //keeps track of the irregular heart beats
                            //assumption: irregular is a variability of ten or more from the previous bpm
                            //if the bpm is not irregular, then the number keeping track of irregular bpm is decremented or stays 0
                            if (lastBpm >= 0 && (bpm <= lastBpm - 10 || bpm >= lastBpm + 10)){
                                irregularBpm++;
                            }else{
                                if(irregularBpm > 0){
                                    irregularBpm--;
                                }
                            }

                            //assumption: if the number keeping track of the irregular bpm gets to 5, it is considered a pattern and an alert is triggerred
                            if(irregularBpm >= 5){
                                triggerAlert(new Alert(patientId, "Trend Alert: Abnormal Heart Rate", record.getTimestamp()));
                            }

                            lastBpm = measurement;
                            break;
                        }
                    }
                    
                    

            }
        }
        
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    public void triggerAlert(Alert alert) {

        alertManager.addAlert(alert); //add the alert to the alert storage via alertManager
        OutputStrategy outputStrategy = new TcpOutputStrategy(3); //not sure what the port number should be
        outputStrategy.output(Integer.parseInt(alert.getPatientId()), alert.getTimestamp(), alert.getCondition(), "triggered");
        // Implementation might involve logging the alert or notifying staff
    }


    public List<Alert> getAlertsPatient(int patientId){
        return alertManager.getAlertsPatient(patientId);
    }
}
