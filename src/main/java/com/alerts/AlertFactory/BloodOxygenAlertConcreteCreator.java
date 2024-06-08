package com.alerts.AlertFactory;

import com.alerts.ConcreteAlert;

/**
 * Represents a blood oxygen alert factory
 */
public class BloodOxygenAlertConcreteCreator extends AlertCreator {

    /**
     * Create a blood oxygen alert
     * @param patientId patient identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     * @return blood oxygen alert
     */
    @Override
    public ConcreteAlert createAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
