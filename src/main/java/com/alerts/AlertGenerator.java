package com.alerts;

import java.util.List;
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
    private AlertManager alertManager;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param alertStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage datastorage, AlertManager alertManager) {
        this.dataStorage = datastorage;;
        this.alertManager = alertManager;
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
    protected void evaluateData(Patient patient) {

        String patientId = patient.getPatientId();
        List<PatientRecord> patientRecord = dataStorage.getRecords(Integer.parseInt(patientId), 0, 2000000000000L); //not sure of the endtime   
        Boolean systolicPressureTooLow = false; //keeps track if the last systolic measurement was too low
        Boolean saturationTooLow = false; //keeps track if the last saturation measurement was too low

        
            PatientRecord record = patientRecord.get(patientRecord.size()-1);
            double measurement = record.getMeasurementValue();

            switch (record.getRecordType()){

                case "DiastolicPressure":
                    evaluateDiastolicPressure(measurement, patientRecord);
                    break;

                case "SystolicPressure" : 
                    evaluateSystolicPressure(measurement, patientRecord);
                    break;

                case "Saturation":
                    evaluateSaturation(measurement, patientRecord);
                    break;

                case "ECG":
                    evaluateECG(patientRecord);
                    break;
            }
        
        
    }

    public void evaluateDiastolicPressure(Double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();

        boolean decreaseInDP = false;
        boolean increaseInDP = false;


        //Treshold check
        if (measurement < 60){
            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Diastolic Pressure too low", timeStamp));
        } else if(measurement > 120){
            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Diastolic Pressure too high", timeStamp));
        }


        //verify if there is a decrease/increase in the measurements over three consecutive measurements
        //if there already was a decrease/increase reported, then it is considered a trend and an alert is triggered
        for(int i = patientRecord.size()-2; i >= 0; i--){

            if(patientRecord.get(i).getRecordType().equals("DiastolicPressure")){

                PatientRecord previousRecord = patientRecord.get(i);
                double previousmeasurement = previousRecord.getMeasurementValue();
                if(measurement < previousmeasurement + 10){
                    if(increaseInDP){//if there was an increase in the diastolic pressure before the decrease, then there is no decrease/increase trend
                        return;
                    } else if(!decreaseInDP){
                        decreaseInDP = true;
                    } else {
                        triggerAlert(new Alert(patientId, "Decreasing Trend Alert in Diastolic Pressure", timeStamp));
                        return;
                    }
                }
                else if (measurement > previousmeasurement -10){
                    if(decreaseInDP){//if there was a decrease in the diastolic pressure before the increase, then there is no decrease/increase trend
                        return;
                    }else if(!increaseInDP){
                        increaseInDP = true;
                    } else {
                        triggerAlert(new Alert(patientId, "Increasing Trend Alert in Diastolic Pressure", timeStamp));
                        return;
                    }
                } else{
                    return;
                }
                measurement = previousmeasurement;
            }
        }
        
    }

    public void evaluateSystolicPressure(double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();

        boolean decreaseInSP = false;
        boolean increaseInSP = false;
        boolean saturationTooLow = false;

        //Treshold check
        if (measurement < 90){
            for(int i = patientRecord.size()-2; i >= 0; i--){
                if(patientRecord.get(i).getRecordType().equals("Saturation")){
                    PatientRecord lastRecord = patientRecord.get(i);
                    double saturation = lastRecord.getMeasurementValue();
                    if(saturation < 92){
                        saturationTooLow = true;
                    }
                    break;
                }
            }
            if(saturationTooLow){
                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Hypotensive Hypoxemia Alert", timeStamp));
            } else{
                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Systolic pressure too low", timeStamp));
            }
        } else if(measurement > 180){
            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Systolic pressure too high", timeStamp));
        }

        //verify if there is a decrease/increase in the measurements
        //if there already was a decrease/increase reported, then it is considered a trend and an alert is triggered
        for(int i = patientRecord.size()-2; i >= 0; i--){

            if(patientRecord.get(i).getRecordType().equals("SystolicPressure")){

                PatientRecord previousRecord = patientRecord.get(i);
                double previousmeasurement = previousRecord.getMeasurementValue();
                if(measurement < previousmeasurement + 10){
                    if(increaseInSP){//if there was an increase in the diastolic pressure before the decrease, then there is no decrease/increase trend
                        return;
                    } else if(!decreaseInSP){
                        decreaseInSP = true;
                    } else {
                        triggerAlert(new Alert(patientId, "Decreasing Trend Alert in Systolic Pressure", timeStamp));
                        return;
                    }
                }
                else if (measurement > previousmeasurement - 10){
                    if(decreaseInSP){//if there was a decrease in the diastolic pressure before the increase, then there is no decrease/increase trend
                        return;
                    } else if(!increaseInSP){
                        increaseInSP = true;
                    } else {
                        triggerAlert(new Alert(patientId, "Increasing Trend Alert in Systolic Pressure", timeStamp));
                        return;
                    }
                } else{
                    return;
                }
                measurement = previousmeasurement;
            }
        }
        
    }

    public void evaluateSaturation(double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();
        boolean systolicPressureTooLow = false;

        //Treshold check
        if(measurement < 92){
            for(int i = patientRecord.size()-2; i >= 0; i--){
                if(patientRecord.get(i).getRecordType().equals("SystolicPressure")){
                    PatientRecord lastRecord = patientRecord.get(i);
                    double systolicPressure = lastRecord.getMeasurementValue();
                    if(systolicPressure < 90){
                        systolicPressureTooLow = true;
                    }
                    break;
                }
            }
            if(systolicPressureTooLow){
                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Hypotensive Hypoxemia Alert", timeStamp));
            } else{
                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Saturation too low", timeStamp));
            }
        }

        PatientRecord patiantData = record;

        //check if the saturation dropped by 5% or more in a window of 10 minutes before the measurement
        for(int t = patientRecord.size()-2; t >= 0 && patiantData.getTimestamp() >= timeStamp - (10*60*1000); t--){
            patiantData = patientRecord.get(t);
            if((patiantData.getRecordType().equals("Saturation")) && (patiantData.getMeasurementValue() >= measurement +5)){
                triggerAlert(new Alert(patientId, "Decreasing Trend Alert in Saturation", patiantData.getTimestamp()));
                break;
            }

        }

        
    }

    public void evaluateECG(List<PatientRecord> patientRecord){
        //find the next ECG measurement to compute the bpm (60/(RR-intervals))
        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        int irregularBpm = 0; //keeps track of the number of consecutive irregular bpm
        for(int k = patientRecord.size()-2; k >=0; k--){
            if(patientRecord.get(k).getRecordType().equals("ECG")){
                PatientRecord previousRecord = patientRecord.get(k);
                double bpm = (60.0/Math.abs(previousRecord.getTimestamp()-record.getTimestamp())) * 1000;

                //Treshold check
                if (bpm < 50){
                    triggerAlert(new Alert(patientId, "Critical Treshold Alert: Heart Rate too low", record.getTimestamp()));
                } else if(bpm > 100){
                    triggerAlert(new Alert(patientId, "Critical Treshold Alert: Heart Rate too high", record.getTimestamp()));
                }


                for(int i = k-1; i >= 0; i--){
                    if(patientRecord.get(i).getRecordType().equals("ECG")){

                        double previousBpm = (60.0/Math.abs(previousRecord.getTimestamp()-patientRecord.get(i).getTimestamp())) * 1000; //keeps track of the last bpm measurement 
                        //keeps track of the irregular heart beats
                        //assumption: irregular is a variability of ten or more from the previous bpm
                        //if the bpm is not irregular, then the number keeping track of irregular bpm is decremented or stays 0
                        if (bpm <= previousBpm - 10 || bpm >= previousBpm + 10){
                            irregularBpm++;
                            
                        }else{
                            if(irregularBpm > 0){
                                irregularBpm--;
                            }
                        }

                        //assumption: if the number keeping track of the irregular bpm gets to 5, it is considered a pattern and an alert is triggerred
                        if(irregularBpm >= 5){
                            triggerAlert(new Alert(patientId, "Trend Alert: Abnormal Heart Rate", patientRecord.get(k).getTimestamp()));
                            return;
                        }
                        bpm = previousBpm;
                        previousRecord = patientRecord.get(i);
                    }
                }
                break;
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
    protected void triggerAlert(Alert alert) {

        System.out.println(alert.getCondition());
        alertManager.addAlert(alert); //add the alert to the alert storage via alertManager
        //OutputStrategy outputStrategy = new TcpOutputStrategy(3); //not sure what the port number should be
        //outputStrategy.output(Integer.parseInt(alert.getPatientId()), alert.getTimestamp(), alert.getCondition(), "triggered");
        // Implementation might involve logging the alert or notifying staff
    }


    protected List<Alert> getAlertsPatient(int patientId){
        return alertManager.getAlertsPatient(patientId);
    }

}