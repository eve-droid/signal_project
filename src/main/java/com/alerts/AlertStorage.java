package com.alerts;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all the current triggered alerts of the sytsem
 *
 *  */
public class AlertStorage{
    protected Map<Integer, List<Alert>> alertMap; // Stores the list of alert objects of each patient indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public AlertStorage() {
        this.alertMap = new HashMap<>();
    }

    /**
     * get and return all the alerts stored
     * 
     * @return the map of the lists of alerts 
     */
    protected Map<Integer, List<Alert>> getAllAlerts(){
        return alertMap;
    }
}
