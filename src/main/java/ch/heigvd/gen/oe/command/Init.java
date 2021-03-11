package ch.heigvd.gen.oe.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "init", description = "Init a static site")
public class Init implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.printf("build");
        return 1;
    }
}
