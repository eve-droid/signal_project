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

        List<PatientRecord> patientRecord = patient.getRecords(0, 20000); //not sure of the endtime

        boolean systolicPressureTooLow = false;;
        boolean saturationTooLow = false;

        for (int i = 0; i < patientRecord.size(); i++){

            PatientRecord record = patientRecord.get(i);
            double measurement = record.getMeasurementValue();
            double lastMeasurement = measurement;
            boolean decrease = false;
            boolean increase = false;

            switch (record.getRecordType()){

                case "DiastolicPressure": 
                    if (measurement < 60){
                        triggerAlert(new Alert(patient.getPatientId(), "Critical Treshold Alert: Diastolic Pressure to low", record.getTimestamp()));
                    } else if(measurement > 120){
                        triggerAlert(new Alert(patient.getPatientId(), "Critical Treshold Alert: Diastolic Pressure to high", record.getTimestamp()));
                    }

                    for (int t = i+1; t < patientRecord.size(); t++){
                        PatientRecord currentRecord = patientRecord.get(t);
                        
                        if (currentRecord.getRecordType().equals("DiastolicPressure")){
                            double currentRecordMeasurement = currentRecord.getMeasurementValue();
                            if(currentRecordMeasurement < lastMeasurement -10){
                                if(!decrease){
                                    decrease = true;
                                } else {
                                    triggerAlert(new Alert(patient.getPatientId(), "Decreasing Trend Alert", record.getTimestamp()));
                                    break;
                                }
                            }

                            else if (currentRecordMeasurement > lastMeasurement +10){
                                if(!increase){
                                    increase = true;
                                } else {
                                    triggerAlert(new Alert(patient.getPatientId(), "Increasing Trend Alert", record.getTimestamp()));
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
                            triggerAlert(new Alert(patient.getPatientId(), "Critical Treshold Alert: Hypotensive Hypoxemia Alert", record.getTimestamp()));
                        } else {
                            triggerAlert(new Alert(patient.getPatientId(), "Critical Treshold Alert: Systolic pressure too low", record.getTimestamp()));
                        }

                        systolicPressureTooLow = true;
                    } else if(measurement > 180){
                        triggerAlert(new Alert(patient.getPatientId(), "Critical Treshold Alert: Systolic pressure too high", record.getTimestamp()));
                    }

                    for (int t = i+1; t < patientRecord.size(); t++){
                        PatientRecord currentRecord = patientRecord.get(t);
                        
                        if (currentRecord.getRecordType().equals("DiastolicPressure")){
                            double currentRecordMeasurement = currentRecord.getMeasurementValue();
                            if(currentRecordMeasurement < lastMeasurement -10){
                                if(!decrease){
                                    decrease = true;
                                } else {
                                    triggerAlert(new Alert(patient.getPatientId(), "Decreasing Trend Alert", currentRecord.getTimestamp()));
                                    break;
                                }
                            }

                            else if (currentRecordMeasurement > lastMeasurement +10){
                                if(!increase){
                                    increase = true;
                                } else {
                                    triggerAlert(new Alert(patient.getPatientId(), "Increasing Trend Alert", currentRecord.getTimestamp()));
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
                            triggerAlert(new Alert(patient.getPatientId(), "Hypotensive Hypoxemia Alert", record.getTimestamp()));
                        } else{
                            triggerAlert(new Alert(patient.getPatientId(), "Critical Treshold Alert: Blood Saturation too low", record.getTimestamp()));
                        }
                        saturationTooLow = true;
                    }

                    long timestamp = record.getTimestamp();
                    PatientRecord currentRecord = record;
                    int t;

                    for(t = i+1; t < patientRecord.size() && currentRecord.getTimestamp() <= timestamp + (10*60*1000); t++){
                        if(patientRecord.get(t).getMeasurementValue() <= measurement -5){
                            triggerAlert(new Alert(patient.getPatientId(), "Decreasing Trend Alert", patientRecord.get(t).getTimestamp()));
                            break;
                        }
                        currentRecord = patientRecord.get(t);
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
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
    }
}
