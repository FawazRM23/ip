package bond.command;

import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Displays the farewell message and requests the end of the console session.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showGoodbyeMessage();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
