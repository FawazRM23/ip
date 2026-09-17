package bond.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bond.task.TaskList;

/**
 * Saves task data to a file on the hard disk.
 */
public class Storage {

    private final Path filePath;

    /**
     * Creates storage that writes to the specified file path.
     *
     * @param filePath Relative or absolute path of the task data file.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Writes every task to the data file, replacing its previous contents.
     *
     * @param taskList Tasks to write in their stored order.
     * @throws IOException If the directory or data file cannot be written.
     */
    public void saveTasks(TaskList taskList) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (int i = 0; i < taskList.getSize(); i++) {
                writer.write(taskList.getTask(i).toDataString());
                writer.newLine();
            }
        }
    }
}
