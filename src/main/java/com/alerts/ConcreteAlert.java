package com.alerts;

/**
 * Represents a concrete alert
 */
public class ConcreteAlert implements Alert{
    private String patientId;
    private String condition;
    private long timestamp;

    /**
     * Create a concrete alert
     * @param patientId patent identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     */
    public ConcreteAlert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Get the patient id
     * @return patient id
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Get the condition
     * @return condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Get the timestamp
     * @return timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set the patient id
     * @param patientId patient id as a string
     */
    public void setTimestamp(long timestamp) {
        this.timestamp =  timestamp;
    }
}
