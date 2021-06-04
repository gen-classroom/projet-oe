package ch.heigvd.gen.oe;

import ch.heigvd.gen.oe.command.Build;
import ch.heigvd.gen.oe.command.Clean;
import ch.heigvd.gen.oe.command.Init;
import ch.heigvd.gen.oe.command.Serve;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * main Command for static site generator
 *
 * @author Gamboni Fiona, Do Vale Lopes Miguel
 */
@Command(name = "projet-oe",
        description = "A brand new static site generator.",
        subcommands = {Init.class, Clean.class, Build.class, Serve.class})
public class Oe implements Callable<Integer> {

    @CommandLine.Option(names = {"-v", "--version"}, description = "version of oe static site generator")
    private static boolean version;

    /**
     * main function
     *
     * @param args arguments provided with 'oe' command
     */
    public static void main(String... args) {
        int exitCode = new CommandLine(new Oe()).execute(args);
        System.exit(exitCode);
    }

    /**
     * Basic call of command oe, treats --version option or invalid arguments
     *
     * @return 0
     */
    @Override
    public Integer call() {

        // Display version if requested
        if (version) {
            final Properties properties = new Properties();
            try {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("oe.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("oe - " + properties.getProperty("version"));
        }

        // No params/options or invalid => display usage
        else {
            CommandLine.usage(this, System.out);
        }
        return 0;
    }
}
