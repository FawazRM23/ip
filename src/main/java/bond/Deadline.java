package bond;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline {

    private final String description;
    private final String by;
    private boolean isDone;

    /**
     * Creates a deadline task that is initially not done.
     *
     * @param description Description of the task.
     * @param by Date or time by which the task must be completed.
     */
    public Deadline(String description, String by) {
        this.description = description;
        this.by = by;
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
     * Returns the task type, status icon, description, and deadline.
     *
     * @return Display form of this deadline task.
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }
}
