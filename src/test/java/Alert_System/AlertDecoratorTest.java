package Alert_System;

import org.junit.Test;

import com.alerts.AlertDecorator.AlertDecorator;
import com.alerts.ConcreteAlert;
import com.alerts.AlertDecorator.PriorityAlertDecorator;
import com.alerts.AlertDecorator.RepeatedAlertDecorator;

import static org.junit.Assert.assertEquals; // Import the assertEquals method

public class AlertDecoratorTest {
    @Test
    public void testPriorityAlertDecorator() {
        // Arrange
        ConcreteAlert alert = new ConcreteAlert("1", "High Heart Rate", 1715250800000L);


        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(alert, 5);
        priorityAlert.setPriority(5);

        assertEquals(5, priorityAlert.getPriority());
    }

    @Test
    public void testAlertDecoratorGetMethods() {
        // Arrange
        ConcreteAlert alert = new ConcreteAlert("1", "High Heart Rate", 1715250800000L);
        AlertDecorator Alert = new AlertDecorator(alert);

        assertEquals("1", Alert.getPatientId());
        assertEquals("High Heart Rate", Alert.getCondition());
        assertEquals(1715250800000L, Alert.getTimestamp());
    }

    @Test
    public void testRepeatedAlertDecorator() {
        // Arrange
        ConcreteAlert alert = new ConcreteAlert("1", "High Heart Rate", 1715250800000L);
        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(alert, 6, 5);
        repeatedAlert.repeatTheAlert();

        assertEquals(5, repeatedAlert.getRepeatCount());

    }
}