package ch.heigvd.gen.oe.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileWatcher {

    private final WatchService watcher;

    public FileWatcher() throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
    }

    public WatchKey watch(List<Path> dirsToWatch) throws IOException, InterruptedException {

        for (Path p : dirsToWatch) {
            p.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        }

        return watcher.take();
    }

}
