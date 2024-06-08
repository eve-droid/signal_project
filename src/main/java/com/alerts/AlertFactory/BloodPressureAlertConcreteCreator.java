package com.alerts.AlertFactory;

import com.alerts.ConcreteAlert;

/**
 * Represents a blood pressure alert factory
 */
public class BloodPressureAlertConcreteCreator extends AlertCreator {

    /**
     * Create a blood pressure alert
     * @param patientId patient identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     * @return blood pressure alert
     */
    @Override
    public ConcreteAlert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}