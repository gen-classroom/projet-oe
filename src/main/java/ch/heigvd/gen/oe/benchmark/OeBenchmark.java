package ch.heigvd.gen.oe.benchmark;

import ch.heigvd.gen.oe.Oe;
import org.openjdk.jmh.annotations.*;
import picocli.CommandLine;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class OeBenchmark {

    private final String DIRNAME = "./benchSite";

    @Setup
    public void setup() {
        new CommandLine(new Oe()).execute("init", DIRNAME);
    }

    @Benchmark
    public void benchmarkBuild() {
        new CommandLine(new Oe()).execute("build", DIRNAME);
    }



}
