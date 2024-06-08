package com.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * This class manages all the alerts (e.g. adding a new alert, deleting one)
 */
public class AlertManager {
    protected Map<Integer, List<ConcreteAlert>> alertMap; // Stores the list of alert objects of each patient indexed by their unique patient ID.
    private AlertGenerator alertGenerator;


    /**
     * Contructs a map to store the list of alert for each patient
     * 
     * @param alertStorage object that stores the alerts
     */
    public AlertManager(){
        AlertStorage alertStorage = new AlertStorage();
        alertMap = alertStorage.getAllAlerts();
        
    }

    /**
     * Evaluates the patient data and triggers an alert if necessary
     * 
     * @param patient patient to evaluate the data for
     * @param dataStorage object that stores the data
     */
    public void evaluateData(Patient patient, DataStorage dataStorage){
        alertGenerator = new AlertGenerator(dataStorage, this);
        alertGenerator.evaluateData(patient);
    }



    /**
     * prints all the alerts stored
     */
    public void printAllAlerts(){
        for(Map.Entry<Integer, List<ConcreteAlert>> entry: alertMap.entrySet()){
            System.out.println("Patient ID: " + entry.getKey());
            for(ConcreteAlert alert: entry.getValue()){
                System.out.println("Condition: " + alert.getCondition() + " Timestamp: " + alert.getTimestamp());
            }
        }
    }

    /**
     * adds an alert to the alert map
     * 
     * @param alert alert to be added
     */
    public void addAlert(ConcreteAlert alert){

        int patientId = Integer.parseInt(alert.getPatientId());
        List<ConcreteAlert> patientList = alertMap.get(patientId);

        if(patientList != null){
            updateAlert(alert);
        } else{ //create new entry if the patient doesn't exist in the map already
            List <ConcreteAlert> list = new ArrayList<>();
            list.add(alert);
            alertMap.put(patientId, list);
        }
    }
    
    /**
     * resolves and deletes the alert from the alertMap (doesn't take into account the timestamp)
     * 
     * @param alert alert to be resolved and deleted
     */
    public void resolveAlert(ConcreteAlert alert){
        int patientId = Integer.parseInt(alert.getPatientId());

        if(alertMap.get(patientId) != null){ //check if the patient has any alert

            if(contain(alertMap.get(patientId), alert)){//check if this alert is present in the alert storage
                for(int i =0; i<alertMap.get(patientId).size(); i++){
                    if(sameAlert(alertMap.get(patientId).get(i), alert)){
                        alertMap.get(patientId).remove(i);
                    }
                }
            } else{
                System.out.println("This alert doesn't exist in the alert storage");
            }
            
        } else{ 
            System.out.println("No alert found for this patient");
        }
        
    } 

    /**
     * checks if two alerts are the same (same condition and patient)
     * 
     * @param alert1 first alert to be compared
     * @param alert2 second alert to be compared
     * 
     * @return true if the two alerts are the same, false otherwise
     */
    public boolean sameAlert(ConcreteAlert alert1, ConcreteAlert alert2){
        if(alert1.getCondition().equals(alert2.getCondition()) && alert1.getPatientId().equals(alert2.getPatientId())){
            return true;
        }
        return false;
    }

    /**
     * gets and returns all the alerts of one patient
     * 
     * @param patientId id of the patient we want the alerts for
     * 
     * @return list of alerts of the patient
     */
    public List<ConcreteAlert> getAlertsPatient(int patientId){
        return alertMap.get(patientId);
    }


    /**
     * update the given alert in the alert storage by changing the timestamp
     * (to avoid duplicate of alerts in the alert storage)
     * assumption: the patient in the alert already exists in the alert storage
     * 
     * @param updatedAlert the alert with the updated timestamp
     */
    public void updateAlert(ConcreteAlert updatedAlert){
        int patientId = Integer.parseInt(updatedAlert.getPatientId());
        List<ConcreteAlert> list = alertMap.get(patientId);

        if(!contain(list, updatedAlert)){//if no similar alert, add the updates alert
            alertMap.get(patientId).add(updatedAlert);
        }

    }


    /**
     * checks if the given alert is present in the list of alerts
     * 
     * @param list list of alerts
     * @param alert alert to be checked
     * 
     * @return true if the alert is present in the list, false otherwise
     */
    public boolean contain(List<ConcreteAlert> list, ConcreteAlert alert){
        for(int i =0; i<list.size(); i++){
            if (sameAlert(alert, list.get(i))){
                return true;
            }
        }
        return false;
    }

    /**
     * gets and returns all the alerts stored
     * 
     * @return map of alerts 
     
    public Map<Integer, List<Alert>> getAllAlerts(){
        return alertMap;
    }*/
}
