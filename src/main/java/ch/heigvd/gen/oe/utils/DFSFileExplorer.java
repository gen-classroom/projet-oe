package ch.heigvd.gen.oe.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class DFSFileExplorer {

    private final Consumer<File> function;
    private final boolean pre;

    public DFSFileExplorer(Consumer<File> function, boolean pre) {
        if (function == null) {
            throw new NullPointerException("Function must exist");
        }
        this.function = function;
        this.pre = pre;
    }

    public void visit(File root) throws InvocationTargetException, IllegalAccessException {
        if (root == null) {
            throw new NullPointerException("Root must exist");
        }
        File[] files = root.listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File f : files) {
                if (pre) {
                    function.accept(root);
                }
                visit(f);
            }
        }
        if (!pre) {
            function.accept(root);
        }
    }
}
