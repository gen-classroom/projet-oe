package ch.heigvd.gen.oe.command;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import ch.heigvd.gen.oe.server.StaticHttpServer;

@Command(name = "serve", description = "Serve a static site")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    @Override
    public Integer call() throws Exception {

        StaticHttpServer server = new StaticHttpServer(dirSiteName);

        server.start();

        while (true) {}

        // return 1;
    }
}
