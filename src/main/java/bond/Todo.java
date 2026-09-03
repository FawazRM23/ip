package bond;

/**
 * Represents a task without an attached date or time.
 */
public class Todo {

    private final String description;
    private boolean isDone;

    /**
     * Creates a to-do task that is initially not done.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        this.description = description;
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
     * Returns the task type, status icon, and description.
     *
     * @return Display form of this to-do task.
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }
}
