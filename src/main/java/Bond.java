import java.util.Scanner;


/**
 * A chatbot that stores tasks entered by the user and displays them on request.
 */
public class Bond {

    private static final int MAX_TASKS = 100;

    /**
     * Starts Bond and processes user commands until the user enters "bye".
     *
     * @param args command-line arguments, which are not used
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
        Task[] tasks = new Task[MAX_TASKS];
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
                tasks[taskIndex].markAsDone();
                System.out.println("    Nice work, agent! Another mission accomplished!:");
                System.out.println("      " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7).trim());
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("    OK, I've marked this mission as not accomplished yet:");
                System.out.println("      " + tasks[taskIndex]);
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
