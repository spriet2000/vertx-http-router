package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.Route;
import com.github.spriet2000.vertx.httprouter.RouteHandler;

import java.util.ArrayList;

public final class Tree {
    private final Node root = new Node();

    public Node root() {
        return root;
    }

    public Tree addNode(String path, RouteHandler handler) {
        try {
            addNode(path, root(), handler);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Duplicate trail: '%s'", path));
        }
        return this;
    }

    public Route find(String path) {
        Route route = new RouteImpl();
        find(path, route, root(), 0, 0);
        return route;
    }

    private void addNode(String path, Node node, RouteHandler handler) throws Exception {
        int matchingChars = 0;
        while (matchingChars < path.length()
                && matchingChars < node.key().length()) {
            if (path.charAt(matchingChars) != node.key().charAt(matchingChars)) {
                break;
            }
            matchingChars++;
        }
        if (node.key().equals("")
                || matchingChars == 0
                || (matchingChars < path.length()
                && matchingChars >= node.key().length())) {

            boolean flag = false;
            String newText = path.substring(matchingChars, path.length());
            for (Node child : node.children()) {
                if (child.key().startsWith(newText.charAt(0) + "")) {
                    flag = true;
                    addNode(newText, child, handler);
                    break;
                }
            }

            if (!flag) {
                Node n = new Node();
                n.key(newText);
                n.value(handler);
                node.children().add(n);
                node.sort();
            }
        } else if (matchingChars == path.length()
                && matchingChars == node.key().length()) {
            throw new Exception("Duplicate trail");
        } else if (matchingChars > 0
                && matchingChars < node.key().length()) {
            Node n1 = new Node();
            n1.key(node.key().substring(matchingChars, node.key().length()));
            n1.value(node.value());
            n1.children(node.children());
            node.value(null);
            node.key(path.substring(0, matchingChars));
            node.children(new ArrayList<>());
            node.children().add(n1);
            node.sort();
            if (matchingChars < path.length()) {
                Node n2 = new Node();
                n2.key(path.substring(matchingChars, path.length()));
                n2.value(handler);
                node.value(null);
                node.children().add(n2);
                node.sort();
            } else {
                node.value(handler);
            }
        } else {
            Node n = new Node();
            n.key(node.key().substring(matchingChars, node.key().length()));
            n.value(node.value());
            n.children(node.children());
            node.value(null);
            node.key(path);
            node.children().add(n);
            node.sort();
        }
    }

    private void find(String path, Route route, Node node, int keyIndex, int pathIndex) {
        int keyLength = node.key().length();
        int pathLength = path.length();

        // ----------------
        // iterate chars

        while (keyIndex < keyLength && isMatchingChar(node.key(), path, keyIndex, pathIndex, pathLength)) {

            // --------------------------
            // check wildcard parameter

            if (node.key().charAt(keyIndex) == '*') {

                // ----------------------------
                // extract wildcard parameter

                String name = node.key().substring(keyIndex, keyLength);
                String value = path.substring(pathIndex, pathLength);

                if (value.isEmpty()) {
                    value = "/";
                } else {
                    if (value.charAt(0) != '/') {
                        value = String.format("/%s", value);
                    }
                }

                // ---------------------
                // set parameter value

                route.parameters().put(name, value);

                // ------------------------------------
                // wildcard is always an endpoint

                route.handler(node.value());
                route.crumb(node.key());

                return;
            }

            // -----------------------
            // check named parameter

            if (node.key().charAt(keyIndex) == ':') {

                // -------------------------
                // extract named parameter

                int eofParamInPrefix = Path.findEndOfParameter(path, pathIndex);
                int eofParamInKey = Path.findEndOfParameter(node.key(), keyIndex);

                String name = node.key().substring(keyIndex, eofParamInKey);
                String value = path.substring(pathIndex, eofParamInPrefix);

                // ---------------------
                // set parameter value

                route.parameters().put(name, value);

                // --------------------------------------------------
                // set indexes to first char after eof parameter

                pathIndex = eofParamInPrefix;
                keyIndex = eofParamInKey;

            } else {
                // -------------
                // next char

                keyIndex += 1;
                pathIndex += 1;
            }
        }

        // ------------------------------
        // trailing slash

        boolean isTrailingSlash = false;
        if (!node.key().isEmpty()
                && pathLength - pathIndex == 1
                && path.charAt(pathLength - 1) == '/') {
            isTrailingSlash = true;
        }

        // ------------
        //  check end

        if (pathLength == 1 && keyLength == 1 && path.charAt(0) == '/' && node.value() == null) {

            // ---------------------------
            // find child node..

            findChildNode(node, "/", route);

        } else if (pathLength == pathIndex || isTrailingSlash) {

            // -----
            // end

            route.handler(node.value());
            route.crumb(node.key());

        } else {

            // ---------------------------
            // find matching child node..

            String newPath = path.substring(pathIndex, pathLength);
            findChildNode(node, newPath, route);
        }
    }

    private void findChildNode(Node node, String path, Route route) {
        for (Node child : node.children()) {
            if (child.key().startsWith(path.charAt(0) + "")
                    || child.key().startsWith(":")
                    || child.key().startsWith("*")) {

                // ---------------
                // found node..

                route.crumb(node.key());
                find(path, route, child, 0, 0);
                break;
            }
        }
    }

    private boolean isMatchingChar(String key, String path, int keyIndex, int pathIndex, int pathLength) {
        return key.charAt(keyIndex) == ':'
                || key.charAt(keyIndex) == '*'
                || pathLength != pathIndex
                && path.charAt(pathIndex) == key.charAt(keyIndex);
    }

}