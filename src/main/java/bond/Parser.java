package bond;

/**
 * Interprets user commands and converts their arguments into domain objects.
 */
public final class Parser {

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK_PREFIX = "mark ";
    private static final String COMMAND_UNMARK_PREFIX = "unmark ";
    private static final String COMMAND_TODO_PREFIX = "todo ";
    private static final String COMMAND_DEADLINE_PREFIX = "deadline ";
    private static final String COMMAND_EVENT_PREFIX = "event ";

    private Parser() {
    }

    /**
     * Returns the type of operation requested by a command.
     *
     * @param command Command entered by the user.
     * @return Type of the requested command.
     */
    public static CommandType getCommandType(String command) {
        if (command.equals(COMMAND_BYE)) {
            return CommandType.BYE;
        }
        if (command.equals(COMMAND_LIST)) {
            return CommandType.LIST;
        }
        if (command.startsWith(COMMAND_MARK_PREFIX)) {
            return CommandType.MARK;
        }
        if (command.startsWith(COMMAND_UNMARK_PREFIX)) {
            return CommandType.UNMARK;
        }
        if (command.startsWith(COMMAND_TODO_PREFIX)) {
            return CommandType.TODO;
        }
        if (command.startsWith(COMMAND_DEADLINE_PREFIX)) {
            return CommandType.DEADLINE;
        }
        if (command.startsWith(COMMAND_EVENT_PREFIX)) {
            return CommandType.EVENT;
        }
        return CommandType.GENERIC;
    }

    /**
     * Returns the zero-based task index specified by a mark or unmark command.
     *
     * @param command Command entered by the user.
     * @param commandType Type of the command.
     * @return Zero-based index of the referenced task.
     */
    public static int getTaskIndex(String command, CommandType commandType) {
        String commandPrefix = switch (commandType) {
            case MARK -> COMMAND_MARK_PREFIX;
            case UNMARK -> COMMAND_UNMARK_PREFIX;
            default -> throw new IllegalArgumentException("Command does not reference a task index");
        };

        int taskNumber = Integer.parseInt(getCommandArgument(command, commandPrefix));
        return taskNumber - 1;
    }

    /**
     * Creates the task described by a task-creation command.
     *
     * @param command Command entered by the user.
     * @param commandType Type of the command.
     * @return Task described by the command.
     */
    public static Task createTask(String command, CommandType commandType) {
        return switch (commandType) {
            case TODO -> new Todo(getCommandArgument(command, COMMAND_TODO_PREFIX));
            case DEADLINE -> createDeadline(command);
            case EVENT -> createEvent(command);
            case GENERIC -> new Task(command);
            default -> throw new IllegalArgumentException("Command does not create a task");
        };
    }

    private static Deadline createDeadline(String command) {
        String deadlineDetails = getCommandArgument(command, COMMAND_DEADLINE_PREFIX);
        String[] deadlineParts = deadlineDetails.split(" /by ", 2);
        String description = deadlineParts[0].trim();
        String by = deadlineParts[1].trim();
        return new Deadline(description, by);
    }

    private static Event createEvent(String command) {
        String eventDetails = getCommandArgument(command, COMMAND_EVENT_PREFIX);
        String[] eventParts = eventDetails.split(" /from ", 2);
        String description = eventParts[0].trim();
        String[] timeParts = eventParts[1].split(" /to ", 2);
        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        return new Event(description, from, to);
    }

    private static String getCommandArgument(String command, String commandPrefix) {
        return command.substring(commandPrefix.length()).trim();
    }
}
