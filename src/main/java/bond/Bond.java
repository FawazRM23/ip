package bond;

import java.nio.file.Path;

import bond.command.AddCommand;
import bond.command.Command;
import bond.command.DeleteCommand;
import bond.command.ExitCommand;
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
        boolean isExit = false;
        while (!isExit) {
            String command = ui.readCommand();
            CommandType commandType = Parser.getCommandType(command);

            ui.showDivider();

            try {
                Command parsedCommand = createCommand(command, commandType);
                parsedCommand.execute(taskList, ui, storage);
                isExit = parsedCommand.isExit();
            } catch (BondException e) {
                ui.showError(e.getMessage(), e.getCorrection());
            } finally {
                ui.showDivider();
            }
        }
    }

    /**
     * Creates the operation requested by a user command.
     *
     * @param command Command entered by the user.
     * @param commandType Type of operation requested by the command.
     * @return Command ready to execute against the current task list.
     * @throws BondException If the user input does not describe a valid command.
     */
    private Command createCommand(String command, CommandType commandType) throws BondException {
        return switch (commandType) {
            case BYE -> new ExitCommand();
            case LIST -> new ListCommand();
            case MARK -> new MarkCommand(
                    Parser.getTaskIndex(command, CommandType.MARK, taskList.getSize()));
            case UNMARK -> new UnmarkCommand(
                    Parser.getTaskIndex(command, CommandType.UNMARK, taskList.getSize()));
            case DELETE -> new DeleteCommand(
                    Parser.getTaskIndex(command, CommandType.DELETE, taskList.getSize()));
            case TODO, DEADLINE, EVENT -> new AddCommand(Parser.createTask(command, commandType));
            case UNKNOWN -> throw new BondException(
                    UNKNOWN_COMMAND_MESSAGE, UNKNOWN_COMMAND_CORRECTION);
        };
    }
}
