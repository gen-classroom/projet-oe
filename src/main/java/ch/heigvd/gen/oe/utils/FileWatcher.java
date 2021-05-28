package ch.heigvd.gen.oe.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing a methode to watch for files inside a site directory
 *
 * @author Do Vale Lopes Miguel, Gamboni Fiona, Tevaearai Rébecca, Zwick Gaétan
 */
public class FileWatcher {

    /**
     * Watch files inside a directory passed as argument in the current file system and block
     * until something was created or deleted or edited
     *
     * @param dirSiteName directory name of the site to watch
     * @throws IOException               - if path could not be register
     * @throws InterruptedException      - if the watcher on the file system could not be initiated
     * @throws InvocationTargetException - if a function could not be called
     * @throws IllegalAccessException    - if access to files/dir is illegal
     */
    public static void watch(String dirSiteName)
            throws IOException, InterruptedException, InvocationTargetException, IllegalAccessException {

        final List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(dirSiteName));
        DFSFileExplorer dfs = new DFSFileExplorer((File file) -> {
            if (file.isDirectory()) {
                paths.add(Paths.get(file.getPath()));
            }
        }, false);
        dfs.visit(new File(dirSiteName + "/pages"));

        WatchService watcher = FileSystems.getDefault().newWatchService();
        for (Path p : paths) {
            p.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        }

        watcher.take().reset();
    }

}
