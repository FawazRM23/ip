package bond;

import java.nio.file.Path;

import bond.exception.BondException;
import bond.exception.StorageException;
import bond.parser.CommandType;
import bond.parser.Parser;
import bond.storage.Storage;
import bond.task.Task;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Coordinates Bond's user interface, command parsing, and task operations.
 */
public class Bond {

    private static final Path DATA_FILE_PATH = Path.of("data", "bond.txt");
    private static final String UNKNOWN_COMMAND_MESSAGE =
            "I don't recognize that command.";
    private static final String UNKNOWN_COMMAND_CORRECTION =
            "Try: todo, deadline, event, list, mark, unmark, delete, or bye.";

    private final Storage storage;
    private final Ui ui;
    /** Tasks loaded at the start of run, before any command is processed. */
    private TaskList taskList;

    /**
     * Creates an application that stores its tasks at the specified relative path.
     *
     * @param filePath Relative path of the task data file.
     */
    public Bond(Path filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
    }

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Bond(DATA_FILE_PATH).run();
    }

    /**
     * Loads tasks and runs one console session, closing input when the session ends.
     * Exits without processing commands if the task archive cannot be loaded.
     */
    public void run() {
        ui.showWelcomeMessage();

        try {
            taskList = storage.loadTasks();
            processCommands();
        } catch (StorageException e) {
            ui.showError(e.getMessage(), e.getCorrection());
            ui.showDivider();
        } finally {
            ui.close();
        }
    }

    /**
     * Reads and executes commands until the user exits Bond.
     */
    private void processCommands() {
        while (true) {
            String command = ui.readCommand();
            CommandType commandType = Parser.getCommandType(command);

            ui.showDivider();

            if (commandType == CommandType.BYE) {
                ui.showGoodbyeMessage();
                break;
            }

            try {
                executeCommand(command, commandType);
            } catch (BondException e) {
                ui.showError(e.getMessage(), e.getCorrection());
            }
            ui.showDivider();
        }
    }

    /**
     * Dispatches a command to the operation that handles it.
     *
     * @param command Command entered by the user.
     * @param commandType Type of operation requested by the command.
     * @throws BondException If the command cannot be executed because of invalid user input.
     */
    private void executeCommand(String command, CommandType commandType) throws BondException {
        switch (commandType) {
            case LIST -> ui.showTaskList(taskList);
            case MARK -> markTask(command);
            case UNMARK -> unmarkTask(command);
            case DELETE -> deleteTask(command);
            case TODO, DEADLINE, EVENT -> addTypedTask(Parser.createTask(command, commandType));
            case UNKNOWN -> throw new BondException(
                    UNKNOWN_COMMAND_MESSAGE, UNKNOWN_COMMAND_CORRECTION);
            default -> throw new IllegalArgumentException("Command type cannot be executed here");
        }
    }

    /**
     * Marks the mission selected by a mark command as complete.
     *
     * @param command Mark command entered by the user.
     * @throws BondException If the command does not select an existing mission.
     */
    private void markTask(String command) throws BondException {
        int taskIndex = Parser.getTaskIndex(command, CommandType.MARK, taskList.getSize());
        Task task = taskList.getTask(taskIndex);
        boolean wasDone = task.isDone();
        task.markAsDone();
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            restoreTaskStatus(task, wasDone);
            throw e;
        }
        ui.showTaskMarked(task);
    }

    /**
     * Marks the mission selected by an unmark command as incomplete.
     *
     * @param command Unmark command entered by the user.
     * @throws BondException If the command does not select an existing mission.
     */
    private void unmarkTask(String command) throws BondException {
        int taskIndex = Parser.getTaskIndex(command, CommandType.UNMARK, taskList.getSize());
        Task task = taskList.getTask(taskIndex);
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            restoreTaskStatus(task, wasDone);
            throw e;
        }
        ui.showTaskUnmarked(task);
    }

    /**
     * Deletes the mission selected by a delete command.
     *
     * @param command Delete command entered by the user.
     * @throws BondException If the command does not select an existing mission.
     */
    private void deleteTask(String command) throws BondException {
        int taskIndex = Parser.getTaskIndex(command, CommandType.DELETE, taskList.getSize());
        Task deletedTask = taskList.deleteTask(taskIndex);
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            taskList.restoreTask(taskIndex, deletedTask);
            throw e;
        }
        ui.showTaskDeleted(deletedTask, taskList.getSize());
    }

    /**
     * Adds a parsed mission to storage and reports the updated mission count.
     *
     * @param task Mission to store.
     * @throws BondException If the updated task list cannot be saved.
     */
    private void addTypedTask(Task task) throws BondException {
        taskList.addTask(task);
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            taskList.removeLastTask();
            throw e;
        }
        ui.showTaskAdded(task, taskList.getSize());
    }

    /**
     * Restores a task's completion status after a failed save.
     *
     * @param task Task whose status must be restored.
     * @param wasDone Completion status before the attempted change.
     */
    private static void restoreTaskStatus(Task task, boolean wasDone) {
        if (wasDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }
}
