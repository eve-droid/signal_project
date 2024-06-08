package com.alerts;

/**
 * Represents a blood pressure alert
 */
public class BloodPressureAlert extends ConcreteAlert{
    
    /**
     * Create a blood pressure alert
     * @param patientId patent identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     */
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
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
