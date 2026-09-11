package bond.parser;

/**
 * Identifies the operation requested by a user command.
 */
public enum CommandType {
    BYE,
    LIST,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    UNKNOWN
}
