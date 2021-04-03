package ch.heigvd.gen.oe.command;

import ch.heigvd.gen.oe.Oe;
import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import ch.heigvd.gen.oe.utils.Markdown;
import org.junit.jupiter.api.*;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Unit tests of command init
 *
 * authors: Gamboni Fiona, Do Vale Lopes Miguel
 */
public class BuildTest {

    private static final String DIR_NAME = "testSite";

    @BeforeAll
    public static void initSite() {
        new CommandLine(new Oe()).execute("init", DIR_NAME);
    }

    @Test
    public void buildShouldCreateBuildDirectoryAndPagesSubDirectory() {
        // Exec command
        new CommandLine(new Oe()).execute("build", DIR_NAME);

        // Check if build exist
        File dir = new File(DIR_NAME + "/build");
        assertTrue(dir.exists());

        // Check if pages exist
        dir = new File(DIR_NAME + "/build/pages");
        assertTrue(dir.exists());
    }

    @Test
    public void buildShouldOnlyTreatIndexOutsideOfPages() {
        // Add other files outside of pages
        File file = null;
        try {
            file = new File(DIR_NAME + "/test.md");
            file.createNewFile();
            file = new File(DIR_NAME + "/image.png");
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Exec command
        new CommandLine(new Oe()).execute("build", DIR_NAME);

        // Check index.html exists
        file = new File(DIR_NAME + "/build/index.html");
        assertTrue(file.exists());

        // Check test.html doesn't exist
        file = new File(DIR_NAME + "build/test.html");
        assertFalse(file.exists());

        // Check image.png doesn't exist
        file = new File(DIR_NAME + "build/image.png");
        assertFalse(file.exists());
    }

    @Test
    public void buildShouldConvertEveryMarkdownInPagesToHtml() {

        // Create dir and markdown files
        Markdown markdown = new Markdown();
        File dir = new File(DIR_NAME + "/pages/dir1");
        dir.mkdir();
        markdown.create(DIR_NAME + "/pages/test1");
        markdown.create(DIR_NAME + "/pages/test2");
        markdown.create(DIR_NAME + "/pages/dir1/test3");

        // Exec command
        new CommandLine(new Oe()).execute("build", DIR_NAME);

        // Check test1.html exists
        File file = new File(DIR_NAME + "/build/pages/test1.html");
        assertTrue(file.exists());

        // Check test2.html exists
        file = new File(DIR_NAME + "/build/pages/test2.html");
        assertTrue(file.exists());

        // Check dir1/test3.html exists
        file = new File(DIR_NAME + "/build/pages/dir1/test3.html");
        assertTrue(file.exists());
    }

    @Test
    public void buildShouldCopyNonMarkdownFilesInPages() {

        // Create dir and files
        File file = null;
        try {
            file = new File(DIR_NAME + "/pages/dir1");
            file.mkdir();
            file = new File(DIR_NAME + "/pages/test.pdf");
            file.createNewFile();
            file = new File(DIR_NAME + "/pages/dir1/image1.png");
            file.createNewFile();
            file = new File(DIR_NAME + "/pages/dir1/image2.jpg");
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Exec command
        new CommandLine(new Oe()).execute("build", DIR_NAME);

        // Check test.pdf exists
        file = new File(DIR_NAME + "/build/pages/test.pdf");
        assertTrue(file.exists());

        // Check dir1/image1.png exists
        file = new File(DIR_NAME + "/build/pages/dir1/image1.png");
        assertTrue(file.exists());

        // Check dir1/image2.jpg exists
        file = new File(DIR_NAME + "/build/pages/dir1/image2.jpg");
        assertTrue(file.exists());

    }

    @AfterEach
    public void cleanBuild() {
        System.out.println("clean build");

        DFSFileExplorer dfsPost = new DFSFileExplorer(File::delete, false);
        try {
            dfsPost.visit(new File(DIR_NAME + "/build"));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
