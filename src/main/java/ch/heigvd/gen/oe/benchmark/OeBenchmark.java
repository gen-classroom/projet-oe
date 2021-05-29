package ch.heigvd.gen.oe.benchmark;

import ch.heigvd.gen.oe.Oe;
import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import org.openjdk.jmh.annotations.*;
import picocli.CommandLine;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * Class regrouping benchmarks on project oe
 *
 * authors: Do Vale Lopes Miguel, Gamboni Fiona
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class OeBenchmark {

    private final String DIRNAME = "./benchSite";

    @Setup
    public void setup() {
        new CommandLine(new Oe()).execute("init", DIRNAME);
    }

    @Benchmark
    public void build() {
        new CommandLine(new Oe()).execute("build", DIRNAME);
    }

    @TearDown
    public void clean() {
        DFSFileExplorer dfsPost = new DFSFileExplorer(File::delete, false);
        try {
            dfsPost.visit(new File(DIRNAME));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
