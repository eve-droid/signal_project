package com.alerts.AlertFactory;

import com.alerts.ConcreteAlert;

/**
 * Represents an ECG alert
 */
public class ECGAlert extends ConcreteAlert{
    
    /**
     * Create an ECG alert
     * @param patientId patient identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     */
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
    
    /**
     * Get the condition of the alert
     * @return condition
     */
    @Override
    public String getCondition() {
        return super.getCondition();
    }
    

}
