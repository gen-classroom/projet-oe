package ch.heigvd.gen.oe;

import ch.heigvd.gen.oe.command.Build;
import ch.heigvd.gen.oe.command.Clean;
import ch.heigvd.gen.oe.command.Init;
import ch.heigvd.gen.oe.command.Serve;

import java.util.Properties;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "projet-oe",
        description = "A brand new static site generator.",
        subcommands = {Init.class, Clean.class, Build.class, Serve.class})
public class Oe implements Callable<Integer> {

    @CommandLine.Option(names = {"-v", "--version"}, description = "version of oe static site generator")
    static boolean version;

    public static void main(String... args) {
        int exitCode = new CommandLine(new Oe()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {

        // Display version if requested
        if (version) {
            final Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("oe.properties"));
            System.out.println("oe - " + properties.getProperty("version"));
        }

        // No Valid params, display usage
        else {
            CommandLine.usage(this, System.out);
        }
        return 0;
    }
}
