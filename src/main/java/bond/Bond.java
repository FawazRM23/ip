package bond;

/**
 * Coordinates Bond's user interface, command parsing, and task operations.
 */
public class Bond {

    private static final String UNKNOWN_COMMAND_MESSAGE =
            "I don't recognize that command.";
    private static final String UNKNOWN_COMMAND_CORRECTION =
            "Try: todo, deadline, event, list, mark, unmark, or bye.";

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList taskList = new TaskList();

        ui.showWelcomeMessage();
        processCommands(ui, taskList);

        ui.close();
    }

    /**
     * Reads and executes commands until the user exits Bond.
     *
     * @param ui Console interface used for input and output.
     * @param taskList Storage for tasks created during the session.
     */
    private static void processCommands(Ui ui, TaskList taskList) {
        while (true) {
            String command = ui.readCommand();
            CommandType commandType = Parser.getCommandType(command);

            ui.showDivider();

            if (commandType == CommandType.BYE) {
                ui.showGoodbyeMessage();
                break;
            }

            try {
                executeCommand(command, commandType, taskList, ui);
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
     * @param ui Console interface used to display results.
     * @throws BondException If the command cannot be executed because of invalid user input.
     */
    private static void executeCommand(String command, CommandType commandType,
            TaskList taskList, Ui ui) throws BondException {
        switch (commandType) {
            case LIST -> ui.showTaskList(taskList);
            case MARK -> markTask(command, taskList, ui);
            case UNMARK -> unmarkTask(command, taskList, ui);
            case TODO, DEADLINE, EVENT ->
                    addTypedTask(Parser.createTask(command, commandType), taskList, ui);
            case UNKNOWN -> throw new BondException(
                    UNKNOWN_COMMAND_MESSAGE, UNKNOWN_COMMAND_CORRECTION);
            default -> throw new IllegalArgumentException("Command type cannot be executed here");
        }
    }

    private static void markTask(String command, TaskList taskList, Ui ui) {
        int taskIndex = Parser.getTaskIndex(command, CommandType.MARK);
        Task task = taskList.getTask(taskIndex);
        task.markAsDone();
        ui.showTaskMarked(task);
    }

    private static void unmarkTask(String command, TaskList taskList, Ui ui) {
        int taskIndex = Parser.getTaskIndex(command, CommandType.UNMARK);
        Task task = taskList.getTask(taskIndex);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);
    }

    private static void addTypedTask(Task task, TaskList taskList, Ui ui) {
        taskList.addTask(task);
        ui.showTaskAdded(task, taskList.getSize());
    }
}
