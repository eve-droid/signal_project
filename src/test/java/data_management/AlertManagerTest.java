package data_management;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.alerts.ConcreteAlert;
import com.alerts.AlertManager;


public class AlertManagerTest {
    
    @Test
    public void testAddAndResolveAlert(){

        AlertManager manager = new AlertManager();

        ConcreteAlert alert1 = new ConcreteAlert("1", "High Heart Rate", 1715250800000L);
        ConcreteAlert alert2 = new ConcreteAlert("1", "High Saturation", 1715250800001L);

        manager.addAlert(alert1);
        manager.addAlert(alert2);
        List<ConcreteAlert> alertList = manager.getAlertsPatient(1);

        assertEquals(2, alertList.size());

        manager.resolveAlert(alert2);
        alertList = manager.getAlertsPatient(1);

        assertEquals(1, alertList.size());

        ConcreteAlert alert3 = new ConcreteAlert("3", "High Saturation", 1715250800001L);
        ConcreteAlert alert4 = new ConcreteAlert("1", "High Diastolic Pressure", 1715250800001L);

        manager.resolveAlert(alert3);
        manager.resolveAlert(alert4);

        assertEquals(1, alertList.size());


    }
}
