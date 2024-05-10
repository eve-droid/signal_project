package com.alerts;

import java.util.List;

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
        List<PatientRecord> patientRecord = dataStorage.getRecords(Integer.parseInt(patientId), 0, 20000); //not sure of the endtime

        boolean systolicPressureTooLow = false;
        boolean saturationTooLow = false;
        int irregularBpm = 0;
        double lastBpm = -1;

        for (int i = 0; i < patientRecord.size(); i++){

            PatientRecord record = patientRecord.get(i);
            double measurement = record.getMeasurementValue();
            double lastMeasurement = measurement;
            boolean decrease = false;
            boolean increase = false;

            switch (record.getRecordType()){

                case "DiastolicPressure": 
                    if (measurement < 60){
                        triggerAlert(new Alert(patientId, "Critical Treshold Alert: Diastolic Pressure to low", record.getTimestamp()));
                    } else if(measurement > 120){
                        triggerAlert(new Alert(patientId, "Critical Treshold Alert: Diastolic Pressure to high", record.getTimestamp()));
                    }

                    for (int t = i+1; t < patientRecord.size(); t++){
                        PatientRecord currentRecord = patientRecord.get(t);
                        
                        if (currentRecord.getRecordType().equals("DiastolicPressure")){
                            double currentRecordMeasurement = currentRecord.getMeasurementValue();
                            if(currentRecordMeasurement < lastMeasurement -10){
                                if(!decrease){
                                    decrease = true;
                                } else {
                                    triggerAlert(new Alert(patientId, "Decreasing Trend Alert", record.getTimestamp()));
                                    break;
                                }
                            }

                            else if (currentRecordMeasurement > lastMeasurement +10){
                                if(!increase){
                                    increase = true;
                                } else {
                                    triggerAlert(new Alert(patientId, "Increasing Trend Alert", record.getTimestamp()));
                                    break;
                                }
                            }

                            else{ break;}

                            lastMeasurement = currentRecordMeasurement;
                        }
                    }
                    break;

                case "SystolicPressure" : 
                    systolicPressureTooLow = false;
                    if (measurement < 90){
                        if(saturationTooLow){
                            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Hypotensive Hypoxemia Alert", record.getTimestamp()));
                        } else {
                            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Systolic pressure too low", record.getTimestamp()));
                        }

                        systolicPressureTooLow = true;
                    } else if(measurement > 180){
                        triggerAlert(new Alert(patientId, "Critical Treshold Alert: Systolic pressure too high", record.getTimestamp()));
                    }

                    for (int t = i+1; t < patientRecord.size(); t++){
                        PatientRecord currentRecord = patientRecord.get(t);
                        
                        if (currentRecord.getRecordType().equals("DiastolicPressure")){
                            double currentRecordMeasurement = currentRecord.getMeasurementValue();
                            if(currentRecordMeasurement < lastMeasurement -10){
                                if(!decrease){
                                    decrease = true;
                                } else {
                                    triggerAlert(new Alert(patientId, "Decreasing Trend Alert", currentRecord.getTimestamp()));
                                    break;
                                }
                            }

                            else if (currentRecordMeasurement > lastMeasurement +10){
                                if(!increase){
                                    increase = true;
                                } else {
                                    triggerAlert(new Alert(patientId, "Increasing Trend Alert", currentRecord.getTimestamp()));
                                    break;
                                }
                            }

                            else{ break;}

                            lastMeasurement = currentRecordMeasurement;
                        }
                    }
                    break;

                case "Saturation":
                    saturationTooLow = false;

                    if(measurement < 92){
                        if(systolicPressureTooLow){
                            triggerAlert(new Alert(patientId, "Hypotensive Hypoxemia Alert", record.getTimestamp()));
                        } else{
                            triggerAlert(new Alert(patientId, "Critical Treshold Alert: Blood Saturation too low", record.getTimestamp()));
                        }
                        saturationTooLow = true;
                    }

                    long timestamp = record.getTimestamp();
                    PatientRecord currentRecord = record;
                    int t;

                    for(t = i+1; t < patientRecord.size() && currentRecord.getTimestamp() <= timestamp + (10*60*1000); t++){
                        if(patientRecord.get(t).getMeasurementValue() <= measurement -5){
                            triggerAlert(new Alert(patientId, "Decreasing Trend Alert", patientRecord.get(t).getTimestamp()));
                            break;
                        }
                        currentRecord = patientRecord.get(t);
                    }
                    break;

                case "ECG":
                    for(int k = i+1; k < patientRecord.size(); k++){
                        if(patientRecord.get(k).getRecordType().equals("ECG")){
                            double bpm = Math.abs(measurement-patientRecord.get(k).getMeasurementValue());

                            if (bpm < 50){
                                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Heart Rate to low", record.getTimestamp()));
                            } else if(bpm > 100){
                                triggerAlert(new Alert(patientId, "Critical Treshold Alert: Heart Rate to high", record.getTimestamp()));
                            }

                            if (lastBpm >= 0 && (bpm <= lastBpm - 10 || bpm >= lastBpm + 10)){
                                irregularBpm++;
                            }else{
                                if(irregularBpm > 0){
                                    irregularBpm--;
                                }
                            }

                            if(irregularBpm >= 5){
                                triggerAlert(new Alert(patientId, "Trend Alert: Abnormal Heart Rate", record.getTimestamp()));
                                irregularBpm = 0;
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

        OutputStrategy outputStrategy = new TcpOutputStrategy(3); //not sure what the port number should be
        outputStrategy.output(Integer.parseInt(alert.getPatientId()), alert.getTimestamp(), alert.getCondition(), "triggered");
        // Implementation might involve logging the alert or notifying staff
    }
}
