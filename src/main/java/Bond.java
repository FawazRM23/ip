import java.util.Scanner;

 //Chatbot  echoes commands until the user enters "bye".

public class Bond {

    // Starts Bond, reads commands, and echoes them to the user.

    public static void main(String[] args) {
        String divider =
                "    ____________________________________________________________";
        String banner = "    ____                  __\n"
                + "   / __ )____  ____  ____/ /\n"
                + "  / __  / __ \\/ __ \\/ __  /\n"
                + " / /_/ / /_/ / / / / /_/ /\n"
                + "/_____/\\____/_/ /_/\\__,_/\n";

        Scanner scanner = new Scanner(System.in);

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

            System.out.println("    " + command);
            System.out.println(divider);
        }

        scanner.close();
    }
}