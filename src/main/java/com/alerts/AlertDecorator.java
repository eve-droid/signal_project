package com.alerts;

/**
 * Represents an alert decorator
 * implements Alert interface
 */
public class AlertDecorator implements Alert{
    
    private Alert alertDecorated;
    
    /**
     * Construct an alert decorator instance
     * @param alertDecorated alert to be decorated
     */
    public AlertDecorator(Alert alertDecorated) {
        this.alertDecorated = alertDecorated;
    }
    
    /**
     * Get the patient id
     * @return patient id
     */
    @Override
    public String getPatientId() {
        return alertDecorated.getPatientId();
    }

    /**
     * Get the condition
     * @return condition
     */
    @Override
    public String getCondition() {
        return alertDecorated.getCondition();
    }

    /**
     * Get the timestamp
     * @return timestamp
     */
    @Override
    public long getTimestamp() {
        return alertDecorated.getTimestamp();
    }

}
