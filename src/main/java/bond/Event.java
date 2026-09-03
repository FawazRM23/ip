package bond;

/**
 * Represents a task that occurs during a specified date or time period.
 */
public class Event {

    private final String description;
    private final String from;
    private final String to;
    private boolean isDone;

    /**
     * Creates an event task that is initially not done.
     *
     * @param description Description of the task.
     * @param from Date or time when the event starts.
     * @param to Date or time when the event ends.
     */
    public Event(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return "X" when done, or a space when not done.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task type, status icon, description, and event period.
     *
     * @return Display form of this event task.
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description + " (from: " + from + " to: " + to + ")";
    }
}
