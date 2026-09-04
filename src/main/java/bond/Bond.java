package bond;

import java.util.Scanner;

/**
 * Provides a chatbot that stores tasks entered by the user and displays them on request.
 */
public class Bond {

    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "    ____                  __\n"
            + "   / __ )____  ____  ____/ /\n"
            + "  / __  / __ \\/ __ \\/ __  /\n"
            + " / /_/ / /_/ / / / / /_/ /\n"
            + "/_____/\\____/_/ /_/\\__,_/\n";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK_PREFIX = "mark ";
    private static final String COMMAND_UNMARK_PREFIX = "unmark ";
    private static final String COMMAND_TODO_PREFIX = "todo ";
    private static final String COMMAND_DEADLINE_PREFIX = "deadline ";
    private static final String COMMAND_EVENT_PREFIX = "event ";

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskList taskList = new TaskList();

        showWelcomeMessage();
        processCommands(scanner, taskList);

        scanner.close();
    }

    /**
     * Displays Bond's welcome message.
     */
    private static void showWelcomeMessage() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("    Good day! I'm Bond, James Bond.");
        System.out.println("    Agent 007 at your service, what can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Reads and executes commands until the user exits Bond.
     *
     * @param scanner Source of user commands.
     * @param taskList Storage for tasks created during the session.
     */
    private static void processCommands(Scanner scanner, TaskList taskList) {
        while (true) {
            String command = scanner.nextLine();

            System.out.println(DIVIDER);

            if (command.equals(COMMAND_BYE)) {
                System.out.println("    Bye. Hope to embark on a mission again soon!");
                System.out.println(DIVIDER);
                break;
            }

            executeCommand(command, taskList);
            System.out.println(DIVIDER);
        }
    }

    /**
     * Dispatches a command to the operation that handles it.
     *
     * @param command Command entered by the user.
     * @param taskList Storage for tasks created during the session.
     */
    private static void executeCommand(String command, TaskList taskList) {
        if (command.equals(COMMAND_LIST)) {
            printTaskList(taskList);
            return;
        }
        if (command.startsWith(COMMAND_MARK_PREFIX)) {
            markTask(command, taskList);
            return;
        }
        if (command.startsWith(COMMAND_UNMARK_PREFIX)) {
            unmarkTask(command, taskList);
            return;
        }
        if (command.startsWith(COMMAND_TODO_PREFIX)) {
            addTodo(command, taskList);
            return;
        }
        if (command.startsWith(COMMAND_DEADLINE_PREFIX)) {
            addDeadline(command, taskList);
            return;
        }
        if (command.startsWith(COMMAND_EVENT_PREFIX)) {
            addEvent(command, taskList);
            return;
        }

        addGenericTask(command, taskList);
    }

    private static void printTaskList(TaskList taskList) {
        System.out.println("    Here are the missions in your list:");
        for (int i = 0; i < taskList.getSize(); i++) {
            System.out.println("    " + (i + 1) + "." + taskList.getTask(i));
        }
    }

    private static void markTask(String command, TaskList taskList) {
        int taskNumber = Integer.parseInt(getCommandArgument(command, COMMAND_MARK_PREFIX));
        int taskIndex = taskNumber - 1;
        Task task = taskList.getTask(taskIndex);
        task.markAsDone();
        System.out.println("    Nice work, agent! Another mission accomplished!:");
        System.out.println("      " + task);
    }

    private static void unmarkTask(String command, TaskList taskList) {
        int taskNumber = Integer.parseInt(getCommandArgument(command, COMMAND_UNMARK_PREFIX));
        int taskIndex = taskNumber - 1;
        Task task = taskList.getTask(taskIndex);
        task.markAsNotDone();
        System.out.println("    OK, I've marked this mission as not accomplished yet:");
        System.out.println("      " + task);
    }

    private static void addTodo(String command, TaskList taskList) {
        String description = getCommandArgument(command, COMMAND_TODO_PREFIX);
        addTypedTask(new Todo(description), taskList);
    }

    private static void addDeadline(String command, TaskList taskList) {
        String deadlineDetails = getCommandArgument(command, COMMAND_DEADLINE_PREFIX);
        String[] deadlineParts = deadlineDetails.split(" /by ", 2);
        String description = deadlineParts[0].trim();
        String by = deadlineParts[1].trim();
        addTypedTask(new Deadline(description, by), taskList);
    }

    private static void addEvent(String command, TaskList taskList) {
        String eventDetails = getCommandArgument(command, COMMAND_EVENT_PREFIX);
        String[] eventParts = eventDetails.split(" /from ", 2);
        String description = eventParts[0].trim();
        String[] timeParts = eventParts[1].split(" /to ", 2);
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        addTypedTask(new Event(description, from, to), taskList);
    }

    private static String getCommandArgument(String command, String commandPrefix) {
        return command.substring(commandPrefix.length()).trim();
    }

    private static void addTypedTask(Task task, TaskList taskList) {
        taskList.addTask(task);
        int taskCount = taskList.getSize();
        String missionNoun = taskCount == 1 ? "mission" : "missions";
        System.out.println("    Got it. I've added this mission:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " " + missionNoun + " in the list.");
    }

    private static void addGenericTask(String command, TaskList taskList) {
        taskList.addTask(new Task(command));
        System.out.println("    added: " + command);
    }
}
