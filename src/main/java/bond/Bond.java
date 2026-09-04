package bond;

/**
 * Coordinates Bond's user interface, command parsing, and task operations.
 */
public class Bond {

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

            executeCommand(command, commandType, taskList, ui);
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
     */
    private static void executeCommand(String command, CommandType commandType,
            TaskList taskList, Ui ui) {
        switch (commandType) {
            case LIST -> ui.showTaskList(taskList);
            case MARK -> markTask(command, taskList, ui);
            case UNMARK -> unmarkTask(command, taskList, ui);
            case TODO, DEADLINE, EVENT ->
                    addTypedTask(Parser.createTask(command, commandType), taskList, ui);
            case GENERIC ->
                    addGenericTask(Parser.createTask(command, commandType), command, taskList, ui);
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

    private static void addGenericTask(Task task, String description, TaskList taskList, Ui ui) {
        taskList.addTask(task);
        ui.showGenericTaskAdded(description);
    }
}
