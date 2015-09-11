package com.github.spriet2000.vertx.httprouter;

import com.github.spriet2000.vertx.httprouter.impl.RouterImpl;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

public interface Router extends Handler<HttpServerRequest> {

    static Router router() {
        return new RouterImpl();
    }

    Router get(String path, RouteHandler handler);

    Router get(String path, Handler<HttpServerRequest> handler);

    Router head(String path, RouteHandler handler);

    Router head(String path, Handler<HttpServerRequest> handler);

    Router post(String path, RouteHandler handler);

    Router post(String path, Handler<HttpServerRequest> handler);

    Router put(String path, RouteHandler handler);

    Router put(String path, Handler<HttpServerRequest> handler);

    Router patch(String path, RouteHandler handler);

    Router patch(String path, Handler<HttpServerRequest> handler);

    Router delete(String path, RouteHandler handler);

    Router delete(String path, Handler<HttpServerRequest> handler);

    void notFoundHandler(Handler<HttpServerRequest> handler);
}
