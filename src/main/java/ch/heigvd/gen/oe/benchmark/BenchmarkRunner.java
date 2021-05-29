package ch.heigvd.gen.oe.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .forks(0)
                .warmupIterations(0)
                .measurementIterations(1)
                .build();
        new Runner(options).run();

    }
}
