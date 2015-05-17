package com.github.spriet2000.vertx.httprouter.impl;

class Path {
    public static int findEndOfParameter(String path, int start) {
        int index;
        for (index = start; index < path.length(); index++) {
            if (path.charAt(index) == '/') {
                return index;
            }
        }
        return index;
    }

    public static String clean(String path) {
        if (path == "") {
            path = "/";
            return path;
        }
        if (path.charAt(0) != '/') {
            path = String.format("/%s", path);
        }
        return path;
    }

    public static String format(String path) {
        return clean(path).toLowerCase();
    }
}
