package com.alerts;

import java.util.List;

import com.alerts.AlertStrategy.BloodOxygenStrategy;
import com.alerts.AlertStrategy.BloodPressureStrategy;
import com.alerts.AlertStrategy.ECGStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private AlertManager alertManager;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param alertStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage datastorage, AlertManager alertManager) {
        this.dataStorage = datastorage;;
        this.alertManager = alertManager;
    }


    
    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    protected void evaluateData(Patient patient) {

        String patientId = patient.getPatientId();
        List<PatientRecord> patientRecord = dataStorage.getRecords(Integer.parseInt(patientId), 0, Long.MAX_VALUE); 


        PatientRecord record = patientRecord.get(patientRecord.size()-1);
        double measurement = record.getMeasurementValue();

        //check the record type to determine which strategy to call
        switch (record.getRecordType()){

            case "Saturation":
                BloodOxygenStrategy oxygenAlertStrategy = new BloodOxygenStrategy(this);
                oxygenAlertStrategy.checkAlert(measurement, patientRecord);
                break;

            case "ECG":
                ECGStrategy ecgAlertStrategy = new ECGStrategy(this);
                ecgAlertStrategy.checkAlert(measurement, patientRecord);
                break;

            default://for diastolic and systolic pressure
                BloodPressureStrategy pressureAlertStrategy = new BloodPressureStrategy(this);
                pressureAlertStrategy.checkAlert(measurement, patientRecord);
                break;
        }
        
    }


    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    public void triggerAlert(ConcreteAlert alert) {

        System.out.println(alert.getCondition());
        alertManager.addAlert(alert); //add the alert to the alert storage via alertManager
        //OutputStrategy outputStrategy = new TcpOutputStrategy(3); //not sure what the port number should be
        //outputStrategy.output(Integer.parseInt(alert.getPatientId()), alert.getTimestamp(), alert.getCondition(), "triggered");
        // Implementation might involve logging the alert or notifying staff
    }


}