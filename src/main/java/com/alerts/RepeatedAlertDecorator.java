package com.alerts;

/**
 * Represents a repeated alert decorator
 */
public class RepeatedAlertDecorator extends AlertDecorator{
    
    private int RepeatCheck;
    private long timeInterval;
    
    /**
     * Create a repeated alert decorator
     * @param alertDecorated alert to be decorated
     * @param timeInterval time interval between repeated alerts as a long
     * @param RepeatCheck number of times to repeat the alert as an integer
     */
    public RepeatedAlertDecorator(Alert alertDecorated, long timeInterval, int RepeatCheck) {
        super(alertDecorated);
        this.RepeatCheck = RepeatCheck;
        this.timeInterval = timeInterval;
    }

    /**
     * repeat the alert for repeatCheck times over timeInterval
     */
    public void repeatTheAlert() {
        for(int i = 0; i < RepeatCheck; i++) {
            repeatAlert();
            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Repeat the alert
     */
    public void repeatAlert() {
        System.out.println("Repeating Alert: " + getCondition() + " for patient " + getPatientId() + " " + (System.currentTimeMillis() - getTimestamp()) + "ms ago");
    }

    
    
}
