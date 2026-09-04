package bond;

import java.util.Scanner;

/**
 * Provides a chatbot that stores tasks entered by the user and displays them on request.
 */
public class Bond {

    private static final int MAX_TASKS = 100;
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "    ____                  __\n"
            + "   / __ )____  ____  ____/ /\n"
            + "  / __  / __ \\/ __ \\/ __  /\n"
            + " / /_/ / /_/ / / / / /_/ /\n"
            + "/_____/\\____/_/ /_/\\__,_/\n";

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];

        showWelcomeMessage();
        processCommands(scanner, tasks);

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
     * @param tasks Storage for tasks created during the session.
     */
    private static void processCommands(Scanner scanner, Task[] tasks) {
        int taskCount = 0;

        while (true) {
            String command = scanner.nextLine();

            System.out.println(DIVIDER);

            if (command.equals("bye")) {
                System.out.println("    Bye. Hope to embark on a mission again soon!");
                System.out.println(DIVIDER);
                break;
            }

            taskCount = executeCommand(command, tasks, taskCount);
            System.out.println(DIVIDER);
        }
    }

    /**
     * Dispatches a command to the operation that handles it.
     *
     * @param command Command entered by the user.
     * @param tasks Storage for tasks created during the session.
     * @param taskCount Number of tasks currently stored.
     * @return Updated number of stored tasks.
     */
    private static int executeCommand(String command, Task[] tasks, int taskCount) {
        if (command.equals("list")) {
            printTaskList(tasks, taskCount);
            return taskCount;
        }
        if (command.startsWith("mark ")) {
            markTask(command, tasks);
            return taskCount;
        }
        if (command.startsWith("unmark ")) {
            unmarkTask(command, tasks);
            return taskCount;
        }
        if (command.startsWith("todo ")) {
            return addTodo(command, tasks, taskCount);
        }
        if (command.startsWith("deadline ")) {
            return addDeadline(command, tasks, taskCount);
        }
        if (command.startsWith("event ")) {
            return addEvent(command, tasks, taskCount);
        }

        return addGenericTask(command, tasks, taskCount);
    }

    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println("    Here are the missions in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("    " + (i + 1) + "." + tasks[i]);
        }
    }

    private static void markTask(String command, Task[] tasks) {
        int taskNumber = Integer.parseInt(command.substring(5).trim());
        int taskIndex = taskNumber - 1;
        tasks[taskIndex].markAsDone();
        System.out.println("    Nice work, agent! Another mission accomplished!:");
        System.out.println("      " + tasks[taskIndex]);
    }

    private static void unmarkTask(String command, Task[] tasks) {
        int taskNumber = Integer.parseInt(command.substring(7).trim());
        int taskIndex = taskNumber - 1;
        tasks[taskIndex].markAsNotDone();
        System.out.println("    OK, I've marked this mission as not accomplished yet:");
        System.out.println("      " + tasks[taskIndex]);
    }

    private static int addTodo(String command, Task[] tasks, int taskCount) {
        String description = command.substring(5).trim();
        return addTypedTask(new Todo(description), tasks, taskCount);
    }

    private static int addDeadline(String command, Task[] tasks, int taskCount) {
        String deadlineDetails = command.substring(9).trim();
        String[] deadlineParts = deadlineDetails.split(" /by ", 2);
        String description = deadlineParts[0].trim();
        String by = deadlineParts[1].trim();
        return addTypedTask(new Deadline(description, by), tasks, taskCount);
    }

    private static int addEvent(String command, Task[] tasks, int taskCount) {
        String eventDetails = command.substring(6).trim();
        String[] eventParts = eventDetails.split(" /from ", 2);
        String description = eventParts[0].trim();
        String[] timeParts = eventParts[1].split(" /to ", 2);
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        return addTypedTask(new Event(description, from, to), tasks, taskCount);
    }

    private static int addTypedTask(Task task, Task[] tasks, int taskCount) {
        tasks[taskCount] = task;
        taskCount++;
        String missionNoun = taskCount == 1 ? "mission" : "missions";
        System.out.println("    Got it. I've added this mission:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " " + missionNoun + " in the list.");
        return taskCount;
    }

    private static int addGenericTask(String command, Task[] tasks, int taskCount) {
        tasks[taskCount] = new Task(command);
        System.out.println("    added: " + command);
        return taskCount + 1;
    }
}
