//Greets user and then exits.
public class Bond {

    //Runs chatbot greeting.

    public static void main(String[] args) {

        String divider = "____________________________________________________________";
        String banner = "    ____                  __\n"
                + "   / __ )____  ____  ____/ /\n"
                + "  / __  / __ \\/ __ \\/ __  /\n"
                + " / /_/ / /_/ / / / / /_/ /\n"
                + "/_____/\\____/_/ /_/\\__,_/\n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Good day! I'm Bond, James Bond.");
        System.out.println("Agent 007 at your service, what can I do for you?");
        System.out.println(divider);
        System.out.println("Mission accomplished. Hope we embark on a mission again soon!");
        System.out.println(divider);
    }
}
