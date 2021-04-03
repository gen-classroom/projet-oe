package ch.heigvd.gen.oe.command;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Subcommand init
 *
 * author: Gamboni Fiona
 */
@Command(name = "clean", description = "Clean a static site")
public class Clean implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to clean site")
    private String dirSiteName;

    /**
     * Call subcommand clean to clean the built site
     *
     * @return 0
     */
    @Override
    public Integer call() {
        DFSFileExplorer dfsPost = new DFSFileExplorer(File::delete, false);
        try {
            dfsPost.visit(new File(dirSiteName + "/build"));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
