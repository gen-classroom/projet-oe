package ch.heigvd.gen.oe.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Class implementing a methode to watch for files
 *
 * @author Do Vale Lopes Miguel, Gamboni Fiona, Tevaearai Rébecca, Zwick Gaétan
 */
public class FileWatcher {

    /**
     * Watch a list of directories passed as argument in the current file system and return
     * a WatchKey associated to the directory that has been created or deleted or modified
     *
     * @param dirsToWatch a List of Path to the directories to watch
     * @return a WatchKey to the directory that has triggered an event
     * @throws IOException          - if path could not be register
     * @throws InterruptedException - if the watcher on the file system could not be initiated
     */
    public static WatchKey watch(List<Path> dirsToWatch) throws IOException, InterruptedException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        for (Path p : dirsToWatch) {
            p.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        }

        return watcher.take();
    }

}
