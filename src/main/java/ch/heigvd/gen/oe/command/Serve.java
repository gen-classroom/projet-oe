package ch.heigvd.gen.oe.command;

import java.io.IOException;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import ch.heigvd.gen.oe.server.StaticHttpServer;

/**
 * Subcommand serve
 *
 * authors: Zwick Gaétan, Tevaearai Rébecca
 */
@Command(name = "serve", description = "Serve a static site")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    /**
     * Call subcommand serve to start an http server for the site at localhost:8080/index.html
     * @return 0
     */
    @Override
    public Integer call() {
        try {
            StaticHttpServer server = new StaticHttpServer(dirSiteName);
            server.start();



        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}
