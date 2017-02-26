package com.github.spriet2000.vertx.httprouter;

import com.github.spriet2000.vertx.httprouter.impl.Tree;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class BenchmarkCompare {

    private Tree router1;
    private RouteMatcher router2;

    @Benchmark
    public void reportTree() throws Exception {

        RouteHandler handler = (request, parameters) -> {
        };

        if (router1 == null) {
            router1 = new Tree()
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
        }

        notNull(router1.find("/"));
        notNull(router1.find("/info/tim/project/java"));
        notNull(router1.find("/search/"));
        notNull(router1.find("/info/eric/project/c"));
        notNull(router1.find("/info/gordon/project/basic"));
        notNull(router1.find("/doc/go1.html"));

    }

    @Benchmark
    public void reportRegex() throws Exception {

        Handler<HttpServerRequest> handler = request -> {
        };

        if (router2 == null) {
            router2 = new RouteMatcher()
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
        }

        notNull(router2.find("/"));
        notNull(router2.find("/info/tim/project/java"));
        notNull(router2.find("/search/"));
        notNull(router2.find("/info/eric/project/c"));
        notNull(router2.find("/info/gordon/project/basic"));
        notNull(router2.find("/doc/go1.html"));
    }

    private void notNull(Route route) throws Exception {
        if (route == null) {
            throw new Exception();
        }
    }

    private void notNull(RouteMatcher.Route route) throws Exception {
        if (route == null) {
            throw new Exception();
        }
    }

}
