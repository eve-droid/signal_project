package com.alerts;

import com.data_management.PatientRecord;
import java.util.List; 

/**
 * Represents an alert strategy for blood oxygen
 */
public class BloodOxygenStrategy implements AlertStrategy{

    private AlertGenerator alertGenerator;

    /**
     * Create a blood oxygen strategy
     * @param alertGenerator alert generator
     */
    public BloodOxygenStrategy(AlertGenerator alertGenerator){
        this.alertGenerator = alertGenerator;
    }
    
    /**
     * Check if an alert should be triggered
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    @Override
    public void checkAlert(double measurement, List<PatientRecord> patientRecord) {
        evaluateSaturation(measurement, patientRecord);
    }

    /**
     * Evaluate the saturation level of the patient
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    public void evaluateSaturation(double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();
        boolean systolicPressureTooLow = false;

        //Treshold check
        if(measurement < 92){
            //check if the last systolic pressure recorded was too low
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
                alertGenerator.triggerAlert(new BloodOxygenAlert(patientId, "Critical Treshold Alert - Hypotensive Hypoxemia Alert", timeStamp));
            } else{
                alertGenerator.triggerAlert(new BloodOxygenAlert(patientId, "Critical Treshold Alert - Saturation too low", timeStamp));
            }
        }

        PatientRecord patiantData = record;

        //check if the saturation dropped by 5% or more in a window of 10 minutes before the measurement
        for(int t = patientRecord.size()-2; t >= 0 && patiantData.getTimestamp() >= timeStamp - (10*60*1000); t--){
            patiantData = patientRecord.get(t);
            if((patiantData.getRecordType().equals("Saturation")) && (patiantData.getMeasurementValue() >= measurement +5)){
                alertGenerator.triggerAlert(new BloodOxygenAlert(patientId, "Decreasing Trend Alert in Saturation", patiantData.getTimestamp()));
                break;
            }

        }

        
    }
}
