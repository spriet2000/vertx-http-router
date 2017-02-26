package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.Route;
import com.github.spriet2000.vertx.httprouter.RouteHandler;
import com.github.spriet2000.vertx.httprouter.Router;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

public class RouterImpl implements Router {
    private Tree getTree;
    private Tree headTree;
    private Tree postTree;
    private Tree putTree;
    private Tree patchTree;
    private Tree deleteTree;

    private Handler<HttpServerRequest> notFoundHandler = (req) -> {
        req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code());
        req.response().end();
    };

    @Override
    public Router get(String path, RouteHandler handler) {
        if (getTree == null) {
            getTree = new Tree();
        }
        getTree.addNode(Path.format(path), handler);
        return this;
    }

    @Override
    public Router get(String path, Handler<HttpServerRequest> handler) {
        return get(path, (req, params) -> handler.handle(req));
    }

    @Override
    public Router head(String path, RouteHandler handler) {
        if (headTree == null) {
            headTree = new Tree();
        }
        headTree.addNode(Path.format(path), handler);
        return this;
    }

    @Override
    public Router head(String path, Handler<HttpServerRequest> handler) {
        return head(path, (req, params) -> handler.handle(req));
    }

    @Override
    public Router post(String path, RouteHandler handler) {
        if (postTree == null) {
            postTree = new Tree();
        }
        postTree.addNode(Path.format(path), handler);
        return this;
    }

    @Override
    public Router post(String path, Handler<HttpServerRequest> handler) {
        return post(path, (req, params) -> handler.handle(req));
    }

    @Override
    public Router put(String path, RouteHandler handler) {
        if (putTree == null) {
            putTree = new Tree();
        }
        putTree.addNode(Path.format(path), handler);
        return this;
    }

    @Override
    public Router put(String path, Handler<HttpServerRequest> handler) {
        return put(path, (req, params) -> handler.handle(req));
    }

    @Override
    public Router patch(String path, RouteHandler handler) {
        if (patchTree == null) {
            patchTree = new Tree();
        }
        patchTree.addNode(Path.format(path), handler);
        return this;
    }

    @Override
    public Router patch(String path, Handler<HttpServerRequest> handler) {
        return patch(path, (req, params) -> handler.handle(req));
    }

    @Override
    public Router delete(String path, RouteHandler handler) {
        if (deleteTree == null) {
            deleteTree = new Tree();
        }
        deleteTree.addNode(Path.format(path), handler);
        return this;
    }

    @Override
    public Router delete(String path, Handler<HttpServerRequest> handler) {
        return delete(path, (req, params) -> handler.handle(req));
    }

    @Override
    public void notFoundHandler(Handler<HttpServerRequest> handler) {
        notFoundHandler = handler;
    }

    @Override
    public void handle(HttpServerRequest request) {
        Route route = route(request.method(), Path.format(request.path()));
        assert route != null;
        RouteHandler handler = route.handler();
        if (handler == null) {
            notFoundHandler.handle(request);
        } else {
            route.handler().handle(request, route.parameters());
        }
    }

    private Route route(HttpMethod method, String path) {
        switch (method) {
            case GET:
                return getTree.find(path);
            case HEAD:
                return headTree.find(path);
            case POST:
                return postTree.find(path);
            case PUT:
                return putTree.find(path);
            case DELETE:
                return deleteTree.find(path);
            case PATCH:
                return patchTree.find(path);
        }
        return null;
    }
}
