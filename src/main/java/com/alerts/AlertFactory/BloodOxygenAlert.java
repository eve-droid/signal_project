package com.alerts.AlertFactory;

import com.alerts.ConcreteAlert;

/**
 * Represents an alert for blood oxygen
 */
public class BloodOxygenAlert extends ConcreteAlert{
    
    /**
     * Create a blood oxygen alert
     * @param patientId patent identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     */
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
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
