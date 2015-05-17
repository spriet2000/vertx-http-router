package com.github.spriet2000.vertx.httprouter;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

import java.util.Map;

@VertxGen
public interface Route {

    Map<String, String> parameters();

    RouteHandler handler();

    String trail();

    void crumb(String path);

    @GenIgnore
    void handler(RouteHandler handler);
}
