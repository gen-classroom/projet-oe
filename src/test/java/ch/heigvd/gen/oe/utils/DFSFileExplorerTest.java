package ch.heigvd.gen.oe.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Unit test of DFSFileExplorer
 *
 * Gamboni Fiona
 */
public class DFSFileExplorerTest{

    private String outputTree = "";

    private void getFileName(File file) {
        outputTree += file.getName() + "\n";
    }

    @Test
    public void DFSFileExplorerShouldNotHaveNullFunction() {
        assertThrows(NullPointerException.class, () -> new DFSFileExplorer(null, false));
    }

    @Test
    public void VisitShouldNotWorkWithNullRoot() {
        DFSFileExplorer dfs = new DFSFileExplorer(this::getFileName, false);
        assertThrows(NullPointerException.class, () -> dfs.visit(null));
    }

    @Test
    public void VisitShouldWorkWithADirInPostOrder() {
        File root = createFileSystem();
        String expectedOutput = "hello.txt\n"
                + "bonDia.json\n"
                + "bonjour.md\n"
                + "subDir1\n"
                + "oe.png\n"
                + "subDir3\n"
                + "subDir2\n"
                + "root\n";

        DFSFileExplorer dfs = new DFSFileExplorer(this::getFileName, false);
        try {
            dfs.visit(root);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
        deleteDir(root);
        assertEquals(expectedOutput, outputTree);
    }

    @Test
    public void VisitShouldWorkWithADirInPreOrder() {
        File root = createFileSystem();
        String expectedOutput = "root\n"
                + "hello.txt\n"
                + "subDir1\n"
                + "bonDia.json\n"
                + "bonjour.md\n"
                + "subDir2\n"
                + "oe.png\n"
                + "subDir3\n";

        DFSFileExplorer dfs = new DFSFileExplorer(this::getFileName, true);
        try {
            dfs.visit(root);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
        deleteDir(root);
        assertEquals(expectedOutput, outputTree);
    }

    private void deleteDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                deleteDir(f);
            }
        }
        dir.delete();
    }

    private File createFileSystem() {
        File root = new File("root");
        root.mkdir();
        File subDir = new File("root/subDir1");
        subDir.mkdir();
        subDir = new File("root/subDir2");
        subDir.mkdir();
        subDir = new File("root/subDir2/subDir3");
        subDir.mkdir();
        File file = new File("root/hello.txt");
        try {
            file.createNewFile();
            file = new File("root/subDir1/bonjour.md");
            file.createNewFile();
            file = new File("root/subDir1/bonDia.json");
            file.createNewFile();
            file = new File("root/subDir2/oe.png");
            file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
            fail();
        }
        return root;
    }
}
