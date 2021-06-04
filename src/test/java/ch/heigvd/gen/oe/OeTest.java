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

        assertTrue(output.toString().startsWith("oe - v"));
    }

    @Test
    public void oeWithInvalidArgumentsShouldReturn2() {
        int exitCode = new CommandLine(new Oe()).execute("fghzetqqr", "yeslife");
        assertEquals(2, exitCode);
    }

}
