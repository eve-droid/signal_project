package com.alerts;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all the current triggered alerts of the sytsem
 * 
 * it extends AlertManager class to facilitate  
 */
public class AlertStorage{
    protected Map<Integer, List<Alert>> alertMap; // Stores the list of alert objects of each patient indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public AlertStorage() {
        this.alertMap = new HashMap<>();
    }

    protected Map<Integer, List<Alert>> getAllAlerts(){
        return alertMap;
    }
}
