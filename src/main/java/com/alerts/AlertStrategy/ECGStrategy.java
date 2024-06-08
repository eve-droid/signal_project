package com.alerts.AlertStrategy;

import com.alerts.AlertGenerator;
import com.alerts.AlertFactory.ECGAlertConcreteCreator;
import com.data_management.PatientRecord;
import java.util.List; 

/**
 * Represents an ECG alert strategy
 */
public class ECGStrategy implements AlertStrategy{

    private ECGAlertConcreteCreator ecgAlertConcreteCreator;
    private AlertGenerator alertGenerator;

    /**
     * Create an ECG strategy
     * @param alertGenerator alert generator
     */
    public ECGStrategy(AlertGenerator alertGenerator){
        this.alertGenerator = alertGenerator;
        this.ecgAlertConcreteCreator = new ECGAlertConcreteCreator();
    }
    
    /**
     * Check if an alert should be triggered
     * @param measurement measurement value of the alert
     * @param patientRecord patient record
     */
    @Override
    public void checkAlert(double measurement, List<PatientRecord> patientRecord) {
        evaluateECG(patientRecord);
    }

    /**
     * Evaluate the ECG of the patient and trigger alerts if heart rate is too low or too high, 
     * or if there is an abnormal trend in the measurements over five consecutive measurements
     * @param patientRecord patient record
     */
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
                    alertGenerator.triggerAlert(ecgAlertConcreteCreator.createAlert(patientId, "Critical Treshold Alert - Heart Rate too low", record.getTimestamp()));
                } else if(bpm > 100){
                    alertGenerator.triggerAlert(ecgAlertConcreteCreator.createAlert(patientId, "Critical Treshold Alert - Heart Rate too high", record.getTimestamp()));
                }


                //verify if there is an abnormal trend in the measurements over five consecutive measurements
                for(int i = k-1; i >= 0; i--){
                    if(patientRecord.get(i).getRecordType().equals("ECG")){

                        double previousBpm = (60.0/Math.abs(previousRecord.getTimestamp()-patientRecord.get(i).getTimestamp())) * 1000; //keeps track of the last bpm measurement 
                        //keeps track of the irregular heart beats
                        //assumption: irregular is a variability of ten or more from the previous bpm
                        //if the bpm is not irregular, then the number keeping track of irregular bpm is decremented or stays 0
                        if (bpm <= previousBpm - 10 || bpm >= previousBpm + 10){
                            irregularBpm++;
                            
                        }else{//if the bpm is not irregular, then the number keeping track of irregular bpm is decremented or stays 0
                            if(irregularBpm > 0){
                                irregularBpm--;
                            }
                        }

                        //assumption: if the number keeping track of the irregular bpm gets to 5, it is considered a pattern and an alert is triggerred
                        if(irregularBpm >= 5){
                            alertGenerator.triggerAlert(ecgAlertConcreteCreator.createAlert(patientId, "Trend Alert - Abnormal Heart Rate", patientRecord.get(k).getTimestamp()));
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
}
