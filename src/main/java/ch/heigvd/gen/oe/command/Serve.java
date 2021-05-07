package ch.heigvd.gen.oe.command;

import java.io.IOException;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import ch.heigvd.gen.oe.server.StaticHttpServer;

@Command(name = "serve", description = "Serve a static site")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    @Override
    public Integer call() {
        try {
            StaticHttpServer server = new StaticHttpServer(dirSiteName);
            boolean serverStarted = server.start();
            while (serverStarted) ;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return 1;
    }
}
