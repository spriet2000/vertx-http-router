package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.Route;
import com.github.spriet2000.vertx.httprouter.RouteHandler;

import java.util.HashMap;
import java.util.Map;

class RouteImpl implements Route {
    private Map<String, String> parameters;
    private RouteHandler handler;
    private String trail = "";

    public RouteHandler handler() {
        return handler;
    }

    public Map<String, String> parameters() {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        return parameters;
    }

    public String trail() {
        return trail;
    }

    public void crumb(String crumb) {
        this.trail += crumb;
    }

    public void handler(RouteHandler handler) {
        this.handler = handler;
    }
}
