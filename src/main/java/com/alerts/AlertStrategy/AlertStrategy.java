package com.alerts.AlertStrategy;

import com.data_management.PatientRecord;
import java.util.List; 

/**
 * Represents an abstract alert strategy
 */
public interface AlertStrategy {

    public void checkAlert(double measurement,List<PatientRecord> patientRecords);
    
}
