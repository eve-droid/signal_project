package com.alerts;

/**
 * Represents an abstract alert factory
 */
public abstract class AlertFactory {
    public abstract ConcreteAlert createAlert(String patientId, String condition, long timestamp);
}



/**
 * Represents a blood pressure alert factory
 */
class BloodPressureAlertFactory extends AlertFactory {

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



/**
 * Represents a blood oxygen alert factory
 */
class BloodOxygenAlertFactory extends AlertFactory {

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



/**
 * Represents an ECG alert factory
 */
class ECGAlertFactory extends AlertFactory {

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