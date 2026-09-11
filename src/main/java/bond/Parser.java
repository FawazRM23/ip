package bond;

/**
 * Interprets user commands and converts their arguments into domain objects.
 */
public final class Parser {

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
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
        if (isCommandWithDetails(command, COMMAND_MARK)) {
            return CommandType.MARK;
        }
        if (isCommandWithDetails(command, COMMAND_UNMARK)) {
            return CommandType.UNMARK;
        }
        if (isCommandWithDetails(command, COMMAND_TODO)) {
            return CommandType.TODO;
        }
        if (isCommandWithDetails(command, COMMAND_DEADLINE)) {
            return CommandType.DEADLINE;
        }
        if (isCommandWithDetails(command, COMMAND_EVENT)) {
            return CommandType.EVENT;
        }
        return CommandType.UNKNOWN;
    }

    /**
     * Returns the zero-based task index specified by a mark or unmark command.
     *
     * @param command Command entered by the user.
     * @param commandType Type of the command.
     * @param taskCount Number of missions available for selection.
     * @return Zero-based index of the referenced task.
     * @throws BondException If the mission number is missing, malformed, or outside the task list.
     */
    public static int getTaskIndex(String command, CommandType commandType,
            int taskCount) throws BondException {
        String commandName = switch (commandType) {
            case MARK -> COMMAND_MARK;
            case UNMARK -> COMMAND_UNMARK;
            default -> throw new IllegalArgumentException("Command does not reference a task index");
        };
        String correction = "Brief me with: " + commandName + " <mission number>.";
        String taskNumberText = getCommandDetails(command, commandName);
        if (taskNumberText.isEmpty()) {
            throw new BondException(
                    "I need a mission number for that " + commandName + " order.", correction);
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new BondException(
                    "That mission number is not a valid whole number.", correction);
        }

        if (taskNumber < 1) {
            throw new BondException("Mission numbers start at 1, agent.", correction);
        }
        if (taskCount == 0) {
            throw new BondException(
                    "The mission dossier is empty.",
                    "Add a mission before trying to " + commandName + " it.");
        }
        if (taskNumber > taskCount) {
            String rangeCorrection = taskCount == 1
                    ? "Choose mission number 1."
                    : "Choose a mission number from 1 to " + taskCount + ".";
            throw new BondException(
                    "Mission " + taskNumber + " is not in the dossier.", rangeCorrection);
        }

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
        String description = getCommandDetails(command, COMMAND_TODO);
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
        String deadlineDetails = getCommandDetails(command, COMMAND_DEADLINE);
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
        String eventDetails = getCommandDetails(command, COMMAND_EVENT);
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
     * Returns whether the input is a supported command, with or without details.
     *
     * @param command Command entered by the user.
     * @param commandName Name of the supported command.
     * @return {@code true} if the input begins with the complete command name.
     */
    private static boolean isCommandWithDetails(String command, String commandName) {
        return command.equals(commandName) || command.startsWith(commandName + " ");
    }

    /**
     * Returns the trimmed details following a command name.
     *
     * @param command Command entered by the user.
     * @param commandName Name of the command.
     * @return Trimmed text following the command name.
     */
    private static String getCommandDetails(String command, String commandName) {
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
}
