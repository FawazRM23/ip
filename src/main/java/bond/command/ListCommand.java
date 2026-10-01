package bond.command;

import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Displays the session's tasks in their stored order without changing them.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTaskList(taskList);
    }
}
