package bond;

import java.util.Scanner;

/**
 * Handles console input and output for Bond.
 */
public class Ui {

    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "    ____                  __\n"
            + "   / __ )____  ____  ____/ /\n"
            + "  / __  / __ \\/ __ \\/ __  /\n"
            + " / /_/ / /_/ / / / / /_/ /\n"
            + "/_____/\\____/_/ /_/\\__,_/\n";

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return Next user command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays Bond's welcome message.
     */
    public void showWelcomeMessage() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("    Good day! I'm Bond, James Bond.");
        System.out.println("    Agent 007 at your service, what can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the divider between commands and responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays Bond's farewell message and the closing divider.
     */
    public void showGoodbyeMessage() {
        System.out.println("    Bye. Hope to embark on a mission again soon!");
        System.out.println(DIVIDER);
    }

    /**
     * Displays all tasks in their stored order.
     *
     * @param taskList Tasks to display.
     */
    public void showTaskList(TaskList taskList) {
        System.out.println("    Here are the missions in your list:");
        for (int i = 0; i < taskList.getSize(); i++) {
            System.out.println("    " + (i + 1) + "." + taskList.getTask(i));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("    Nice work, agent! Another mission accomplished!:");
        System.out.println("      " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("    OK, I've marked this mission as not accomplished yet:");
        System.out.println("      " + task);
    }

    /**
     * Displays confirmation that a typed task was added.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        String missionNoun = taskCount == 1 ? "mission" : "missions";
        System.out.println("    Got it. I've added this mission:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " " + missionNoun + " in the list.");
    }

    /**
     * Displays confirmation that a generic task was added.
     *
     * @param description Description of the task that was added.
     */
    public void showGenericTaskAdded(String description) {
        System.out.println("    added: " + description);
    }

    /**
     * Closes the input scanner used by this UI.
     */
    public void close() {
        scanner.close();
    }
}
