package bond;

/**
 * Stores the tasks created during a Bond session.
 */
public class TaskList {

    private static final int MAX_TASKS = 100;

    private final Task[] tasks;
    private int size;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new Task[MAX_TASKS];
        size = 0;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        tasks[size] = task;
        size++;
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based position of the task.
     * @return Task at the specified index.
     */
    public Task getTask(int index) {
        return tasks[index];
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of tasks in the list.
     */
    public int getSize() {
        return size;
    }
}
