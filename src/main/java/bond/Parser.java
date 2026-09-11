package bond;

/**
 * Interprets user commands and converts their arguments into domain objects.
 */
public final class Parser {

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK_PREFIX = "mark ";
    private static final String COMMAND_UNMARK_PREFIX = "unmark ";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final String DEADLINE_BY_MARKER = "/by";
    private static final String EVENT_FROM_MARKER = "/from";
    private static final String EVENT_TO_MARKER = "/to";

    private static final String TODO_CORRECTION =
            "Brief me with: todo <description>.";
    private static final String DEADLINE_CORRECTION =
            "Brief me with: deadline <description> /by <date or time>.";
    private static final String EVENT_CORRECTION =
            "Brief me with: event <description> /from <start> /to <end>.";

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
        if (isTaskCreationCommand(command, COMMAND_TODO)) {
            return CommandType.TODO;
        }
        if (isTaskCreationCommand(command, COMMAND_DEADLINE)) {
            return CommandType.DEADLINE;
        }
        if (isTaskCreationCommand(command, COMMAND_EVENT)) {
            return CommandType.EVENT;
        }
        return CommandType.UNKNOWN;
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
     * @throws BondException If required task details are missing.
     */
    public static Task createTask(String command, CommandType commandType) throws BondException {
        return switch (commandType) {
            case TODO -> createTodo(command);
            case DEADLINE -> createDeadline(command);
            case EVENT -> createEvent(command);
            default -> throw new IllegalArgumentException("Command does not create a task");
        };
    }

    /**
     * Creates a to-do task after validating its description.
     *
     * @param command Todo command entered by the user.
     * @return Todo described by the command.
     * @throws BondException If the description is empty.
     */
    private static Todo createTodo(String command) throws BondException {
        String description = getTaskDetails(command, COMMAND_TODO);
        if (description.isEmpty()) {
            throw new BondException(
                    "This todo mission has no description.", TODO_CORRECTION);
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline task after validating its description and deadline.
     *
     * @param command Deadline command entered by the user.
     * @return Deadline described by the command.
     * @throws BondException If required deadline details are missing.
     */
    private static Deadline createDeadline(String command) throws BondException {
        String deadlineDetails = getTaskDetails(command, COMMAND_DEADLINE);
        if (deadlineDetails.isEmpty()) {
            throw new BondException(
                    "This deadline mission has no description.", DEADLINE_CORRECTION);
        }

        int byMarkerIndex = findMarkerIndex(deadlineDetails, DEADLINE_BY_MARKER);
        if (byMarkerIndex == -1) {
            throw new BondException(
                    "This deadline mission is missing its /by marker.", DEADLINE_CORRECTION);
        }

        String description = deadlineDetails.substring(0, byMarkerIndex).trim();
        String by = deadlineDetails
                .substring(byMarkerIndex + DEADLINE_BY_MARKER.length())
                .trim();
        if (description.isEmpty()) {
            throw new BondException(
                    "This deadline mission has no description.", DEADLINE_CORRECTION);
        }
        if (by.isEmpty()) {
            throw new BondException(
                    "This deadline mission has no date or time.", DEADLINE_CORRECTION);
        }
        return new Deadline(description, by);
    }

    /**
     * Creates an event task after validating its description and time period.
     *
     * @param command Event command entered by the user.
     * @return Event described by the command.
     * @throws BondException If required event details are missing.
     */
    private static Event createEvent(String command) throws BondException {
        String eventDetails = getTaskDetails(command, COMMAND_EVENT);
        if (eventDetails.isEmpty()) {
            throw new BondException(
                    "This event mission has no description.", EVENT_CORRECTION);
        }

        int fromMarkerIndex = findMarkerIndex(eventDetails, EVENT_FROM_MARKER);
        if (fromMarkerIndex == -1) {
            throw new BondException(
                    "This event mission is missing its /from marker.", EVENT_CORRECTION);
        }

        String description = eventDetails.substring(0, fromMarkerIndex).trim();
        String eventPeriod = eventDetails
                .substring(fromMarkerIndex + EVENT_FROM_MARKER.length())
                .trim();
        if (description.isEmpty()) {
            throw new BondException(
                    "This event mission has no description.", EVENT_CORRECTION);
        }
        if (eventPeriod.isEmpty()) {
            throw new BondException(
                    "This event mission has no starting date or time.", EVENT_CORRECTION);
        }

        int toMarkerIndex = findMarkerIndex(eventPeriod, EVENT_TO_MARKER);
        if (toMarkerIndex == -1) {
            throw new BondException(
                    "This event mission is missing its /to marker.", EVENT_CORRECTION);
        }

        String from = eventPeriod.substring(0, toMarkerIndex).trim();
        String to = eventPeriod
                .substring(toMarkerIndex + EVENT_TO_MARKER.length())
                .trim();
        if (from.isEmpty()) {
            throw new BondException(
                    "This event mission has no starting date or time.", EVENT_CORRECTION);
        }
        if (to.isEmpty()) {
            throw new BondException(
                    "This event mission has no ending date or time.", EVENT_CORRECTION);
        }
        return new Event(description, from, to);
    }

    /**
     * Returns whether the input is a task-creation command, with or without details.
     *
     * @param command Command entered by the user.
     * @param commandName Name of the task-creation command.
     * @return True if the input begins with the complete command name.
     */
    private static boolean isTaskCreationCommand(String command, String commandName) {
        return command.equals(commandName) || command.startsWith(commandName + " ");
    }

    /**
     * Returns the trimmed details following a task-creation command name.
     *
     * @param command Command entered by the user.
     * @param commandName Name of the task-creation command.
     * @return Trimmed text following the command name.
     */
    private static String getTaskDetails(String command, String commandName) {
        return command.substring(commandName.length()).trim();
    }

    /**
     * Returns the position of a standalone syntax marker in task details.
     *
     * @param details Task details that may contain the marker.
     * @param marker Syntax marker to locate.
     * @return Marker position, or -1 if the marker is not a separate token.
     */
    private static int findMarkerIndex(String details, String marker) {
        int markerIndex = details.indexOf(marker);
        while (markerIndex != -1) {
            int markerEndIndex = markerIndex + marker.length();
            boolean hasLeadingBoundary = markerIndex == 0
                    || Character.isWhitespace(details.charAt(markerIndex - 1));
            boolean hasTrailingBoundary = markerEndIndex == details.length()
                    || Character.isWhitespace(details.charAt(markerEndIndex));
            if (hasLeadingBoundary && hasTrailingBoundary) {
                return markerIndex;
            }
            markerIndex = details.indexOf(marker, markerIndex + 1);
        }
        return -1;
    }

    private static String getCommandArgument(String command, String commandPrefix) {
        return command.substring(commandPrefix.length()).trim();
    }
}
