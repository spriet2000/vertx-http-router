package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.Route;
import com.github.spriet2000.vertx.httprouter.RouteHandler;

import java.util.ArrayList;
import java.util.Objects;

public class Tree {
    private Node root;

    public Node root() {
        if (root == null) {
            root = new Node();
        }
        return root;
    }

    public Tree add(String path, RouteHandler handler) {
        try {
            add(path, root(), handler);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Duplicate trail: '%s'", path));
        }
        return this;
    }

    public Route find(String path) {
        Route route = new RouteImpl();
        find(path, route, root());
        return route;
    }

    private void add(String path, Node node, RouteHandler handler) throws Exception {
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
                    add(newText, child, handler);
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

    private void find(String path, Route route, Node node) {
        int keyLength = node.key().length();
        int pathLength = path.length();
        int keyIndex = 0;
        int pathIndex = 0;

        // ---------------
        // iterate chars

        while (keyIndex < keyLength && pathIndex < pathLength
                && (node.key().charAt(keyIndex) == ':'
                || node.key().charAt(keyIndex) == '*'
                || path.charAt(pathIndex) == node.key().charAt(keyIndex))) {

            // --------------------------
            // check wildcard parameter

            if (node.key().charAt(keyIndex) == '*') {

                // ----------------------------
                // extract wildcard parameter

                String name = node.key().substring(keyIndex, keyLength);
                String value = path.substring(pathIndex, pathLength);
                route.parameters().put(name, value.charAt(0) != '/' ? String.format("/%s", value) : value);

                // ---------
                // endpoint

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
                route.parameters().put(name, path.substring(pathIndex, eofParamInPrefix));

                // -------------
                // set indexes

                pathIndex = eofParamInPrefix - 1;
                keyIndex = eofParamInKey - 1;

            }

            // ------
            // next

            keyIndex += 1;
            pathIndex += 1;
        }

        // ------------
        //  check end

        if (pathIndex == pathLength
                && keyIndex == keyLength) {

            if (node.value() != null){

                // -----------
                // endpoint..

                route.handler(node.value());
                route.crumb(node.key());

                return;
            }

            // ---------------------------------------
            // check if child starts with parameter

            node.children().forEach(c -> {
                String first = String.valueOf(c.key().charAt(0));
                if (Objects.equals(first, ":")
                        || Objects.equals(first, "*")){
                    find(first , route, c);
                }
            });

            return;

        }

        // ------------------------------------------------
        // char doesn't match, still path chars to iterate

        if (pathIndex < pathLength) {

            // ------------------------------
            // trailing slash

            if (keyLength > 0
                    && pathIndex + 1 == pathLength
                    && path.charAt(pathIndex) == '/') {

                // ------------
                // endpoint..

                route.handler(node.value());
                route.crumb(node.key());

                return;
            }

            // ----------------------------
            // search children of current

            String newText = path.substring(pathIndex, pathLength);
            for (Node child : node.children()) {
                if (child.key().startsWith(newText.charAt(0) + "")
                        || child.key().startsWith(":")
                        || child.key().startsWith("*")) {
                    route.crumb(node.key().substring(0, keyIndex));
                    find(newText, route, child);
                    break;
                }
            }
            return;
        }

        // ------------------------------
        // still key chars to iterate

        if (keyIndex < keyLength) {

            // ------------------------------
            // trailing slash

            if (keyIndex + 1 == keyLength
                    && node.key().charAt(keyIndex) == '/') {

                // ------------
                // endpoint..

                route.handler(node.value());
                route.crumb(node.key());

                return;
            }

            // ------------------------------
            // check parameters in key

            while (keyIndex < keyLength) {
                if (node.key().charAt(keyIndex) == '*') {

                    // ----------------------------
                    // extract wildcard parameter

                    route.parameters().put(node.key().substring(keyIndex, keyLength), "/");

                    // ------------
                    // endpoint..

                    route.handler(node.value());
                    route.crumb(node.key());

                    return;
                }
                if (node.key().charAt(keyIndex) == ':') {

                    // -------------------------
                    // extract named parameter

                    int eofParamInKey = Path.findEndOfParameter(node.key(), keyIndex);
                    route.parameters().put(node.key().substring(keyIndex, eofParamInKey), "");

                    // -------------
                    // set indexes

                    keyIndex = eofParamInKey - 1;

                }
                keyIndex += 1;
            }
        }
    }
}