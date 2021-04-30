package ch.heigvd.gen.oe.command;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "serve", description = "Serve a static site")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    @Override
    public Integer call() throws Exception {

        SimpleHttpServer server = new SimpleHttpServer(dirSiteName);

        server.start();

        return 1;
    }
}
