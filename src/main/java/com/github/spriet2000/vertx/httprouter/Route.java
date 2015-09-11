package com.github.spriet2000.vertx.httprouter;

import java.util.Map;

public interface Route {

    Map<String, String> parameters();

    RouteHandler handler();

    String trail();

    void crumb(String path);

    void handler(RouteHandler handler);
}
