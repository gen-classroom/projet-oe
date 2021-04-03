package ch.heigvd.gen.oe;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

/**
 * Unit tests of command oe
 *
 * authors: Gamboni Fiona, Do Vale Lopes Miguel
 */
public class OeTest {

    @Test
    public void oeWithOptionVersionShouldWork() {
        // Redirects stdout to output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        new CommandLine(new Oe()).execute("--version");

        // Revert to stdout
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        //System.out.println(output.toString());
        assertTrue(output.toString().startsWith("oe - v"));
    }

    @Test
    public void oeWithInvalidArgumentsShouldReturn2() {
        int exitCode = new CommandLine(new Oe()).execute("fghzetqqr", "yeslife");
        assertEquals(2, exitCode);
    }

    // TODO ask for help
    /*
    @Test
    public void oeWithoutArgumentsShouldDisplayUsage() {
        final String USAGE = "Usage: projet-oe [-v] [COMMAND]\n"
                + "A brand new static site generator.\n"
                + "  -v, --version   version of oe static site generator\n"
                + "Commands:\n"
                + "  init   Init a static site\n"
                + "  clean  Clean a static site\n"
                + "  build  Build a static site\n"
                + "  serve  Serve a static site\n";

        // Redirects stdout to output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        int exitCode = new CommandLine(new Oe()).execute();

        // Revert to stdout
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.out.println(output.toString());
        assertEquals(0, exitCode);
        assertEquals(USAGE, output.toString());
    }

     */

}
