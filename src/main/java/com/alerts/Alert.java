package com.alerts;

/**
 * Represents an abstract alert
 */
public interface Alert {
    
    String getPatientId();
    String getCondition();
    long getTimestamp();
}
