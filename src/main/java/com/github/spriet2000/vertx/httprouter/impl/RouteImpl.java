package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.Route;
import com.github.spriet2000.vertx.httprouter.RouteHandler;

import java.util.HashMap;
import java.util.Map;

class RouteImpl implements Route {

    private StringBuilder trail;

    private Map<String, String> parameters;
    private RouteHandler handler;

    private boolean useTrail;

    RouteImpl(boolean useTrail) {
        this.useTrail = useTrail;
        if (useTrail) {
            trail = new StringBuilder();
        }
    }

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
        if (useTrail) {
            return trail.toString();
        }
        return "";
    }

    public void crumb(String crumb) {
        if (useTrail) {
            this.trail.append(crumb);
        }
    }

    public void handler(RouteHandler handler) {
        this.handler = handler;
    }
}
