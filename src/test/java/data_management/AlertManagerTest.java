package data_management;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.alerts.Alert;
import com.alerts.AlertManager;
import com.alerts.AlertStorage;

public class AlertManagerTest {
    
    @Test
    public void testAddAndResolveAlert(){

        AlertManager manager = new AlertManager(new AlertStorage());

        Alert alert1 = new Alert("1", "High Heart Rate", 1715250800000L);
        Alert alert2 = new Alert("1", "High Saturation", 1715250800001L);

        manager.addAlert(alert1);
        manager.addAlert(alert2);
        List<Alert> alertList = manager.getAlertsPatient(1);

        assertEquals(2, alertList.size());

        manager.resolveAlert(alert2);
        alertList = manager.getAlertsPatient(1);

        assertEquals(1, alertList.size());

        Alert alert3 = new Alert("3", "High Saturation", 1715250800001L);
        Alert alert4 = new Alert("1", "High Diastolic Pressure", 1715250800001L);

        manager.resolveAlert(alert3);
        manager.resolveAlert(alert4);

        assertEquals(1, alertList.size());


    }
}
