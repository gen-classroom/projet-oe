package ch.heigvd.gen.oe.command;

import ch.heigvd.gen.oe.Oe;
import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests of command clean
 *
 * author: Gamboni Fiona
 */
public class CleanTest {

    private static final String DIR_NAME = "testSite";

    @BeforeAll
    public static void initAndBuildSite() {
        new CommandLine(new Oe()).execute("init", DIR_NAME);
        new CommandLine(new Oe()).execute("build", DIR_NAME);
    }

    @Test
    public void CleanShouldDeleteBuildDir() {
        new CommandLine(new Oe()).execute("clean", DIR_NAME);

        // Check if build exist
        File dir = new File(DIR_NAME + "/build");
        assertFalse(dir.exists());
    }

    @AfterAll
    public static void cleanSite() {
        System.out.println("clean testSite");

        DFSFileExplorer dfsPost = new DFSFileExplorer(File::delete, false);
        try {
            dfsPost.visit(new File(DIR_NAME));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
