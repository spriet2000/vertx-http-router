package com.github.spriet2000.vertx.httprouter;

import com.github.spriet2000.vertx.httprouter.impl.Tree;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.openjdk.jmh.annotations.Benchmark;

public class BenchmarkCompare {

    @Benchmark
    public void reportTree() throws Exception {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        Route route = tree.find("/info/gordon/project/java");

        if (route.handler() == null) {
            throw new Exception();
        }

    }

    @Benchmark
    public void reportRegex() throws Exception {
        Handler<HttpServerRequest> handler = request -> {
        };

        RouteMatcher matcher = new RouteMatcher();

        matcher.add("/", handler)
                .add("/cmd/:tool/:sub", handler)
                .add("/cmd/:tool/", handler)
                .add("/src/*filepath", handler)
                .add("/search/", handler)
                .add("/search/:query", handler)
                .add("/user_:name", handler)
                .add("/user_:name/about", handler)
                .add("/files/:dir/*filepath", handler)
                .add("/cmd/:tool/:sub", handler)
                .add("/doc/", handler)
                .add("/cmd/:tool/:sub", handler)
                .add("/doc/go_faq.html", handler)
                .add("/doc/go1.html", handler)
                .add("/info/:user/public", handler)
                .add("/info/:user/project/:project", handler);

        RouteMatcher.Route route = matcher.find("/info/gordon/project/java");

        if (route == null) {
            throw new Exception();
        }
    }


}
