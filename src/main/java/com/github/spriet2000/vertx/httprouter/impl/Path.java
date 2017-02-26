package com.github.spriet2000.vertx.httprouter.impl;

import java.util.Objects;

class Path {
    static int findEndOfParameter(String path, int start) {
        int index;
        for (index = start; index < path.length(); index++) {
            if (path.charAt(index) == '/') {
                return index;
            }
        }
        return index;
    }

    private static String clean(String path) {
        if (Objects.equals(path, "")) {
            path = "/";
            return path;
        }
        if (path.charAt(0) != '/') {
            path = String.format("/%s", path);
        }
        return path;
    }

    static String format(String path) {
        return clean(path).toLowerCase();
    }
}
