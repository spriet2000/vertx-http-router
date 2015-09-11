package com.github.spriet2000.vertx.httprouter;

import io.vertx.core.http.HttpServerRequest;

import java.util.Map;

public interface RouteHandler {

    void handle(HttpServerRequest request, Map<String, String> parameters);
}
