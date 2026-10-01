package bond.command;

import bond.exception.BondException;
import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Represents an operation that can be executed against a Bond session.
 */
public abstract class Command {

    /**
     * Executes the operation using the session's tasks, user interface, and storage.
     *
     * @param taskList Tasks available in the current session.
     * @param ui User interface used to display the result.
     * @param storage File storage used by operations that change tasks.
     * @throws BondException If the operation cannot be completed.
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws BondException;
}
