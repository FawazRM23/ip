package bond.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bond.exception.BondException;
import bond.task.Deadline;
import bond.task.Event;
import bond.task.Task;
import bond.task.TaskList;
import bond.task.Todo;

/**
 * Loads and saves task data in a file on the hard disk.
 */
public class Storage {

    private static final String FIELD_DELIMITER_PATTERN = " \\| ";
    private static final String TASK_TYPE_TODO = "T";
    private static final String TASK_TYPE_DEADLINE = "D";
    private static final String TASK_TYPE_EVENT = "E";
    private static final String STATUS_DONE = "1";

    private final Path filePath;

    /**
     * Creates storage that writes to the specified file path.
     *
     * @param filePath Relative or absolute path of the task data file.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads all tasks from the data file in their stored order.
     *
     * @return Loaded tasks, or an empty task list when no data file exists.
     * @throws IOException If the data file cannot be read.
     * @throws BondException If the data file contains more tasks than the list can store.
     */
    public TaskList loadTasks() throws IOException, BondException {
        TaskList taskList = new TaskList();
        if (!Files.exists(filePath)) {
            return taskList;
        }

        for (String taskData : Files.readAllLines(filePath)) {
            taskList.addTask(parseTask(taskData));
        }
        return taskList;
    }

    /**
     * Writes every task to the data file, replacing its previous contents.
     *
     * @param taskList Tasks to write in their stored order.
     * @throws IOException If the directory or data file cannot be written.
     */
    public void saveTasks(TaskList taskList) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (int i = 0; i < taskList.getSize(); i++) {
                writer.write(taskList.getTask(i).toDataString());
                writer.newLine();
            }
        }
    }

    /**
     * Converts one line of stored task data into its corresponding task.
     *
     * @param taskData Stored task data to parse.
     * @return Task represented by the stored data.
     */
    private Task parseTask(String taskData) {
        String[] fields = taskData.split(FIELD_DELIMITER_PATTERN);
        String taskType = fields[0];
        boolean isDone = fields[1].equals(STATUS_DONE);
        String description = fields[2];

        Task task = switch (taskType) {
            case TASK_TYPE_TODO -> new Todo(description);
            case TASK_TYPE_DEADLINE -> new Deadline(description, fields[3]);
            case TASK_TYPE_EVENT -> new Event(description, fields[3], fields[4]);
            default -> throw new IllegalArgumentException("Unknown stored task type: " + taskType);
        };

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
