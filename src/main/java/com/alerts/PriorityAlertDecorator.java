package com.alerts;

/**
 * Represents a priority alert decorator
 */
public class PriorityAlertDecorator extends AlertDecorator{
    
    private int priority;
    
    /**
     * Create a priority alert decorator
     * @param alertDecorated alert to be decorated
     * @param priority priority of the alert as an integer
     */
    public PriorityAlertDecorator(Alert alertDecorated, int priority) {
        super(alertDecorated);
        this.priority = priority;
    }

    /**
     * Get the priority of the alert
     * @return priority
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Set the priority of the alert
     * @param priority priority of the alert as an integer
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    
}
