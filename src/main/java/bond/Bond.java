package bond;

import java.util.Scanner;

/**
 * Provides a chatbot that stores tasks entered by the user and displays them on request.
 */
public class Bond {

    private static final int MAX_TASKS = 100;

    /**
     * Marks either a generic task or a to-do task as done.
     *
     * @param task Task to mark as done.
     */
    private static void markAsDone(Object task) {
        if (task instanceof Task genericTask) {
            genericTask.markAsDone();
        } else if (task instanceof Todo todo) {
            todo.markAsDone();
        }
    }

    /**
     * Marks either a generic task or a to-do task as not done.
     *
     * @param task Task to mark as not done.
     */
    private static void markAsNotDone(Object task) {
        if (task instanceof Task genericTask) {
            genericTask.markAsNotDone();
        } else if (task instanceof Todo todo) {
            todo.markAsNotDone();
        }
    }

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        String divider =
                "    ____________________________________________________________";
        String banner = "    ____                  __\n"
                + "   / __ )____  ____  ____/ /\n"
                + "  / __  / __ \\/ __ \\/ __  /\n"
                + " / /_/ / /_/ / / / / /_/ /\n"
                + "/_____/\\____/_/ /_/\\__,_/\n";

        Scanner scanner = new Scanner(System.in);
        Object[] tasks = new Object[MAX_TASKS];
        int taskCount = 0;

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("    Good day! I'm Bond, James Bond.");
        System.out.println("    Agent 007 at your service, what can I do for you?");
        System.out.println(divider);

        while (true) {
            String command = scanner.nextLine();

            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("    Bye. Hope to embark on a mission again soon!");
                System.out.println(divider);
                break;
            }

            if (command.equals("list")) {
                System.out.println("    Here are the missions in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("    " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5).trim());
                int taskIndex = taskNumber - 1;
                markAsDone(tasks[taskIndex]);
                System.out.println("    Nice work, agent! Another mission accomplished!:");
                System.out.println("      " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7).trim());
                int taskIndex = taskNumber - 1;
                markAsNotDone(tasks[taskIndex]);
                System.out.println("    OK, I've marked this mission as not accomplished yet:");
                System.out.println("      " + tasks[taskIndex]);
            } else if (command.startsWith("todo ")) {
                String description = command.substring(5).trim();
                tasks[taskCount] = new Todo(description);
                taskCount++;
                String missionNoun = taskCount == 1 ? "mission" : "missions";
                System.out.println("    Got it. I've added this mission:");
                System.out.println("      " + tasks[taskCount - 1]);
                System.out.println("    Now you have " + taskCount + " " + missionNoun + " in the list.");
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("    added: " + command);
            }

            System.out.println(divider);
        }

        scanner.close();
    }
}
