package ch.heigvd.gen.oe.command;

import ch.heigvd.gen.oe.Oe;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * Unit tests of command init
 *
 * @author Do Vale Lopes Miguel
 */
public class InitTest {

    private void deleteDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                deleteDir(f);
            }
        }
        dir.delete();
    }

    @Test
    public void initShouldCreateSiteDirectoryWithRelativePath() {
        final String DIR_NAME = "testSite";

        // Exec command
        new CommandLine(new Oe()).execute("init", DIR_NAME);

        // Check if dir exist
        File dir = new File("./" + DIR_NAME);
        boolean dirExists = dir.exists();

        // Clean
        if (dirExists) {
            deleteDir(dir);
        }

        assertTrue(dirExists);
    }

    @Test
    public void initShouldCreateSiteDirectoryWithAbsolutePath() {
        final String DIR_NAME = "testSite";
        final String CURRENT_DIR = System.getProperty("user.dir");

        // Exec command
        new CommandLine(new Oe()).execute("init", CURRENT_DIR + "/" + DIR_NAME);

        // Check if dir exist
        File dir = new File(CURRENT_DIR + "/" + DIR_NAME);
        boolean dirExists = dir.exists();

        // Clean
        if (dirExists) {
            deleteDir(dir);
        }

        assertTrue(dirExists);
    }

    @Test
    public void initShouldCreateTheCorrectFilesAndDir() {
        final String SITE_DIRNAME = "testSite";
        final String[] GOALS = {"pages", "index.md", "config.json", "templates", "templates/layout.html", "templates/menu.html"};

        // Exec command
        new CommandLine(new Oe()).execute("init", SITE_DIRNAME);

        // Check if each file exists inside repository SITE_DIRNAME
        boolean fileNotPresent = false;
        for (String filename : GOALS) {
            File file = new File(SITE_DIRNAME + "/" + filename);
            if (!file.exists()){
                fileNotPresent = true;
                break;
            }
        }

        // Clean
        deleteDir(new File(SITE_DIRNAME));

        assertFalse(fileNotPresent);
    }

}
