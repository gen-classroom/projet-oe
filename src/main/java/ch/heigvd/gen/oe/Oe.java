package ch.heigvd.gen.oe;

import ch.heigvd.gen.oe.command.Build;
import ch.heigvd.gen.oe.command.Clean;
import ch.heigvd.gen.oe.command.Init;
import ch.heigvd.gen.oe.command.Serve;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "projet-oe",
        description = "A brand new static site generator.",
        subcommands = {Init.class, Clean.class, Build.class, Serve.class})
public class Oe implements Callable<Integer> {

    public static void main(String... args) {
        int exitCode = new CommandLine(new Oe()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out);
        return 0;
    }
}
