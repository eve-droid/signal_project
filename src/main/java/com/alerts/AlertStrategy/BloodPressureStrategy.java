package com.alerts.AlertStrategy;

import com.alerts.AlertGenerator;
import com.alerts.AlertFactory.BloodPressureAlertConcreteCreator;
import com.data_management.PatientRecord;
import java.util.List; 

/**
 * Represents an alert strategy for blood pressure
 */
public class BloodPressureStrategy implements AlertStrategy{

    private BloodPressureAlertConcreteCreator bloodPressureAlertFactory;
    private AlertGenerator alertGenerator;

    /**
     * Create a blood pressure strategy
     * @param alertGenerator alert generator
     */
    public BloodPressureStrategy(AlertGenerator alertGenerator){
        this.alertGenerator = alertGenerator;  
        this.bloodPressureAlertFactory = new BloodPressureAlertConcreteCreator();
    }
    
    /**
     * Check if an alert should be triggered
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    @Override
    public void checkAlert(double measurement, List<PatientRecord> patientRecord) {
        evaluateDiastolicPressure(measurement, patientRecord);
        evaluateSystolicPressure(measurement, patientRecord);
    }

    /**
     * Evaluate the diastolic pressure of the patient and trigger alerts if pressure is too low or too high, 
     * or if there is a decreasing/increasing trend in the measurements over three consecutive measurements
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    public void evaluateDiastolicPressure(Double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();

        boolean decreaseInDP = false;
        boolean increaseInDP = false;


        //Treshold check
        if (measurement < 60){
            alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Critical Treshold Alert - Diastolic Pressure too low", timeStamp));
        } else if(measurement > 120){
            alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Critical Treshold Alert - Diastolic Pressure too high", timeStamp));
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
                        alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Decreasing Trend Alert in Diastolic Pressure", timeStamp));
                        return;
                    }
                }
                else if (measurement > previousmeasurement -10){
                    if(decreaseInDP){//if there was a decrease in the diastolic pressure before the increase, then there is no decrease/increase trend
                        return;
                    }else if(!increaseInDP){
                        increaseInDP = true;
                    } else {
                        alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Increasing Trend Alert in Diastolic Pressure", timeStamp));
                        return;
                    }
                } else{
                    return;
                }
                measurement = previousmeasurement;
            }
        }
        
    }

    /**
     * Evaluate the systolic pressure of the patient and trigger alerts if pressure is too low or too high, 
     * or if there is a decreasing/increasing trend in the measurements over three consecutive measurements
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    public void evaluateSystolicPressure(double measurement, List<PatientRecord> patientRecord){

        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        String patientId = Integer.toString(record.getPatientId());
        long timeStamp = record.getTimestamp();

        boolean decreaseInSP = false;
        boolean increaseInSP = false;
        boolean saturationTooLow = false;

        //Treshold check
        if (measurement < 90){
            //check if the last saturation recorded was too low
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
                alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Critical Treshold Alert - Hypotensive Hypoxemia Alert", timeStamp));
            } else{
                alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Critical Treshold Alert - Systolic pressure too low", timeStamp));
            }
        } else if(measurement > 180){
            alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Critical Treshold Alert  Systolic pressure too high", timeStamp));
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
                        alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Decreasing Trend Alert in Systolic Pressure", timeStamp));
                        return;
                    }
                }
                else if (measurement > previousmeasurement - 10){
                    if(decreaseInSP){//if there was a decrease in the diastolic pressure before the increase, then there is no decrease/increase trend
                        return;
                    } else if(!increaseInSP){
                        increaseInSP = true;
                    } else {
                        alertGenerator.triggerAlert(bloodPressureAlertFactory.createAlert(patientId, "Increasing Trend Alert in Systolic Pressure", timeStamp));
                        return;
                    }
                } else{
                    return;
                }
                measurement = previousmeasurement;
            }
        }
        
    }
}
