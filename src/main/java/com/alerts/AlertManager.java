package com.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertManager {
    protected Map<Integer, List<Alert>> alertMap; // Stores the list of alert objects of each patient indexed by their unique patient ID.

    public AlertManager(AlertStorage alertStorage){
        alertMap = alertStorage.getAllAlerts();
    }

    public void addAlert(Alert alert){

        int patientId = Integer.parseInt(alert.getPatientId());
        
        if(alertMap.get(patientId) != null){
            alertMap.get(Integer.parseInt(alert.getPatientId())).add(alert);
        } else{
            List <Alert> list = new ArrayList<>();
            list.add(alert);
            alertMap.put(patientId, list);
        }
    }
    
    public void resolveAlert(Alert alert){
        int patientId = Integer.parseInt(alert.getPatientId());

        for(int i =0; i<alertMap.get(patientId).size(); i++){
            if(sameAlert(alertMap.get(patientId).get(i), alert)){
                alertMap.get(patientId).remove(i);
            }
        }
    } 

    public boolean sameAlert(Alert alert1, Alert alert2){
        if(alert1.getCondition().equals(alert2.getCondition()) && alert1.getPatientId().equals(alert2.getPatientId()) && alert1.getTimestamp() == alert2.getTimestamp()){
            return true;
        }
        return false;
    }

    public List<Alert> getAlertsPatient(int patientId){
        return alertMap.get(patientId);
    }
}
