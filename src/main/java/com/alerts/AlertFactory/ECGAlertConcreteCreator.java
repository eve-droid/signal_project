package com.alerts.AlertFactory;

import com.alerts.ConcreteAlert;

/**
 * Represents an ECG alert factory
 */
public class ECGAlertConcreteCreator extends AlertCreator {

    /**
     * Create an ECG alert
     * @param patientId patient identifier as a string
     * @param condition condition of the alert as a string
     * @param timestamp timestamp of the alert as a long
     * @return ECG alert
     */
    @Override
    public ConcreteAlert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}