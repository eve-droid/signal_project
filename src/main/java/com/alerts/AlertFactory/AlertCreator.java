package com.alerts.AlertFactory;

import com.alerts.ConcreteAlert;

/**
 * Represents an abstract alert factory
 */
public abstract class AlertCreator {
    public abstract ConcreteAlert createAlert(String patientId, String condition, long timestamp);
}


