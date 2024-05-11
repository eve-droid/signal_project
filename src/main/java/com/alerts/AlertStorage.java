package com.alerts;


import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the current triggered alerts of the sytsem
 * 
 * it extends AlertManager class to facilitate  
 */
public class AlertStorage extends AlertManager {
    protected List<Alert> AlertList; // Stores alert objects

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public AlertStorage() {
        this.AlertList = new ArrayList<>();
    }

    public List<Alert> getAlerts(){
        return AlertList;
    }
}
