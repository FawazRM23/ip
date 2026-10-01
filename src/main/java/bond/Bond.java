package bond;

import java.nio.file.Path;

import bond.command.AddCommand;
import bond.command.Command;
import bond.command.DeleteCommand;
import bond.command.ListCommand;
import bond.command.MarkCommand;
import bond.command.UnmarkCommand;
import bond.exception.BondException;
import bond.exception.StorageException;
import bond.parser.CommandType;
import bond.parser.Parser;
import bond.storage.Storage;
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
            case LIST -> {
                Command listCommand = new ListCommand();
                listCommand.execute(taskList, ui, storage);
            }
            case MARK -> {
                int taskIndex = Parser.getTaskIndex(command, CommandType.MARK, taskList.getSize());
                Command markCommand = new MarkCommand(taskIndex);
                markCommand.execute(taskList, ui, storage);
            }
            case UNMARK -> {
                int taskIndex = Parser.getTaskIndex(command, CommandType.UNMARK, taskList.getSize());
                Command unmarkCommand = new UnmarkCommand(taskIndex);
                unmarkCommand.execute(taskList, ui, storage);
            }
            case DELETE -> {
                int taskIndex = Parser.getTaskIndex(command, CommandType.DELETE, taskList.getSize());
                Command deleteCommand = new DeleteCommand(taskIndex);
                deleteCommand.execute(taskList, ui, storage);
            }
            case TODO, DEADLINE, EVENT -> {
                Command addCommand = new AddCommand(Parser.createTask(command, commandType));
                addCommand.execute(taskList, ui, storage);
            }
            case UNKNOWN -> throw new BondException(
                    UNKNOWN_COMMAND_MESSAGE, UNKNOWN_COMMAND_CORRECTION);
            default -> throw new IllegalArgumentException("Command type cannot be executed here");
        }
    }
}
