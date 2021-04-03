package ch.heigvd.gen.oe.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;


/**
 * Class DFSFileExplorer to DFS a file system in pre or post order
 * 
 * authors: Gamboni Fiona, Do Vale Lopes Miguel
 */
public class DFSFileExplorer {

    private final Consumer<File> function;
    private final boolean pre;

    /**
     * Constructor
     *
     * @param function function to use while exploring
     * @param pre      boolean to tell if function will be used in pre order (false for post order)
     */
    public DFSFileExplorer(Consumer<File> function, boolean pre) {
        if (function == null) {
            throw new NullPointerException("Function must exist");
        }
        this.function = function;
        this.pre = pre;
    }

    /**
     * Visit file system and apply function to each file
     *
     * @param root root directory
     * @throws InvocationTargetException - if function could not be invoked
     * @throws IllegalAccessException    - if function could not be accessed
     */
    public void visit(File root) throws InvocationTargetException, IllegalAccessException {
        if (root == null) {
            throw new NullPointerException("Root must exist");
        }

        if (pre) {
            function.accept(root);
        }

        File[] files = root.listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File f : files) {
                visit(f);
            }
        }
        if (!pre) {
            function.accept(root);
        }
    }
}
