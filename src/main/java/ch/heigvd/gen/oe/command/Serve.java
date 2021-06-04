package ch.heigvd.gen.oe.command;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

import ch.heigvd.gen.oe.Oe;
import ch.heigvd.gen.oe.utils.FileWatcher;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import ch.heigvd.gen.oe.server.StaticHttpServer;

/**
 * Subcommand serve
 *
 * @author Zwick Gaétan, Tevaearai Rébecca
 */
@Command(name = "serve", description = "Serve a static site")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    @CommandLine.Option(names = {"-w", "--watch"}, description = "build site every update")
    private static boolean watch;

    /**
     * Call subcommand serve to start an http server for the site at localhost:8080/index.html
     *
     * @return 0
     */
    @Override
    public Integer call() {
        try {
            StaticHttpServer server = new StaticHttpServer(dirSiteName);
            server.start();

            if (watch) {
                // Big brother is watching
                do {
                    FileWatcher.watch(dirSiteName);

                    // Clean and build
                    build();
                } while(true);
            }

        } catch (InvocationTargetException | IllegalAccessException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    /**
     * Clean command
     */
    private void build() {
        new CommandLine(new Oe()).execute("build", dirSiteName);
    }
}
