package com.alerts;

import java.util.List;

public class AlertManager {
    private List<Alert> alertList;

    public AlertManager(AlertStorage alertStorage){
        alertList = alertStorage.getAlerts();
    }

    public AlertManager(List<Alert> alertList){
        this.alertList = alertList;
    }

    public void addAlert(Alert alert){
        alertList.add(alert);
    }
    
    public void resolveAlert(Alert alert){
        for(int i =0; i<alertList.size(); i++){
            if(sameAlert(alertList.get(i), alert)){
                alertList.remove(i);
            }
        }
    } 

    public boolean sameAlert(Alert alert1, Alert alert2){
        if(alert1.getCondition().equals(alert2.getCondition()) && alert1.getPatientId().equals(alert2.getPatientId()) && alert1.getTimestamp() == alert2.getTimestamp()){
            return true;
        }
        return false;
    }
}
