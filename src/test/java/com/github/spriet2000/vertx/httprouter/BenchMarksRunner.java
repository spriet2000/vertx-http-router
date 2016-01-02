package com.github.spriet2000.vertx.httprouter;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;

public class BenchMarksRunner {

    public static void main(String... args) throws IOException, RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + BenchMark1.class.getSimpleName() + ".*")
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
