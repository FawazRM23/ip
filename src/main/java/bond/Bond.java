package bond;

import java.io.IOException;

import bond.exception.BondException;
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

    private static final String DATA_FILE_PATH = "./data/bond.txt";
    private static final String UNKNOWN_COMMAND_MESSAGE =
            "I don't recognize that command.";
    private static final String UNKNOWN_COMMAND_CORRECTION =
            "Try: todo, deadline, event, list, mark, unmark, or bye.";

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args Command-line arguments, which are not used.
     * @throws IOException If task data cannot be read from or written to the hard disk.
     * @throws BondException If the saved task list exceeds the supported capacity.
     */
    public static void main(String[] args) throws IOException, BondException {
        Ui ui = new Ui();
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList taskList = storage.loadTasks();

        ui.showWelcomeMessage();
        processCommands(ui, taskList, storage);

        ui.close();
    }

    /**
     * Reads and executes commands until the user exits Bond.
     *
     * @param ui Console interface used for input and output.
     * @param taskList Storage for tasks created during the session.
     * @param storage File storage used to save task-list changes.
     * @throws IOException If task data cannot be written to the hard disk.
     */
    private static void processCommands(Ui ui, TaskList taskList, Storage storage)
            throws IOException {
        while (true) {
            String command = ui.readCommand();
            CommandType commandType = Parser.getCommandType(command);

            ui.showDivider();

            if (commandType == CommandType.BYE) {
                ui.showGoodbyeMessage();
                break;
            }

            try {
                executeCommand(command, commandType, taskList, storage, ui);
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
     * @param taskList Storage for tasks created during the session.
     * @param storage File storage used to save task-list changes.
     * @param ui Console interface used to display results.
     * @throws BondException If the command cannot be executed because of invalid user input.
     * @throws IOException If task data cannot be written to the hard disk.
     */
    private static void executeCommand(String command, CommandType commandType,
            TaskList taskList, Storage storage, Ui ui) throws BondException, IOException {
        switch (commandType) {
            case LIST -> ui.showTaskList(taskList);
            case MARK -> markTask(command, taskList, storage, ui);
            case UNMARK -> unmarkTask(command, taskList, storage, ui);
            case TODO, DEADLINE, EVENT ->
                    addTypedTask(Parser.createTask(command, commandType), taskList, storage, ui);
            case UNKNOWN -> throw new BondException(
                    UNKNOWN_COMMAND_MESSAGE, UNKNOWN_COMMAND_CORRECTION);
            default -> throw new IllegalArgumentException("Command type cannot be executed here");
        }
    }

    /**
     * Marks the mission selected by a mark command as complete.
     *
     * @param command Mark command entered by the user.
     * @param taskList Storage containing the selected mission.
     * @param storage File storage used to save the changed task list.
     * @param ui Console interface used to display the result.
     * @throws BondException If the command does not select an existing mission.
     * @throws IOException If task data cannot be written to the hard disk.
     */
    private static void markTask(String command, TaskList taskList, Storage storage, Ui ui)
            throws BondException, IOException {
        int taskIndex = Parser.getTaskIndex(command, CommandType.MARK, taskList.getSize());
        Task task = taskList.getTask(taskIndex);
        task.markAsDone();
        storage.saveTasks(taskList);
        ui.showTaskMarked(task);
    }

    /**
     * Marks the mission selected by an unmark command as incomplete.
     *
     * @param command Unmark command entered by the user.
     * @param taskList Storage containing the selected mission.
     * @param storage File storage used to save the changed task list.
     * @param ui Console interface used to display the result.
     * @throws BondException If the command does not select an existing mission.
     * @throws IOException If task data cannot be written to the hard disk.
     */
    private static void unmarkTask(String command, TaskList taskList, Storage storage, Ui ui)
            throws BondException, IOException {
        int taskIndex = Parser.getTaskIndex(command, CommandType.UNMARK, taskList.getSize());
        Task task = taskList.getTask(taskIndex);
        task.markAsNotDone();
        storage.saveTasks(taskList);
        ui.showTaskUnmarked(task);
    }

    /**
     * Adds a parsed mission to storage and reports the updated mission count.
     *
     * @param task Mission to store.
     * @param taskList Storage for missions created during the session.
     * @param storage File storage used to save the changed task list.
     * @param ui Console interface used to display the result.
     * @throws BondException If the mission dossier has reached its capacity.
     * @throws IOException If task data cannot be written to the hard disk.
     */
    private static void addTypedTask(Task task, TaskList taskList, Storage storage, Ui ui)
            throws BondException, IOException {
        taskList.addTask(task);
        storage.saveTasks(taskList);
        ui.showTaskAdded(task, taskList.getSize());
    }
}
