package com.github.spriet2000.vertx.httprouter;

import com.github.spriet2000.vertx.httprouter.impl.Tree;
import org.openjdk.jmh.annotations.Benchmark;

public class BenchMark1 {

    @Benchmark
    public void report1() throws Exception {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .add("/", handler)
                .add("/cmd/:tool/:sub", handler)
                .add("/cmd/:tool/", handler)
                .add("/src/*filepath", handler)
                .add("/search/", handler)
                .add("/search/:query", handler)
                .add("/user_:name", handler)
                .add("/user_:name/about", handler)
                .add("/files/:dir/*filepath", handler)
                .add("/doc/", handler)
                .add("/doc/go_faq.html", handler)
                .add("/doc/go1.html", handler)
                .add("/info/:user/public", handler)
                .add("/info/:user/project/:project", handler);

        Route route = tree.find("/info/gordon/project/java");

        if(route.handler() == null){
            throw new Exception();
        }

    }
}