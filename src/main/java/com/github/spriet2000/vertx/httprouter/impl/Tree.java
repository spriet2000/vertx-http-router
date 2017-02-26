package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.Route;
import com.github.spriet2000.vertx.httprouter.RouteHandler;

import java.util.ArrayList;
import java.util.List;

import static com.github.spriet2000.vertx.httprouter.impl.Utils.eofPartIndex;

public final class Tree {
    private final Node root = new Node();

    Node root() {
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
        return find(path, false);
    }

    public Route find(String path, boolean useTrail) {
        Route route = new RouteImpl(useTrail);
        find(path, route, root());
        return route;
    }

    private void find(String path, Route route, Node node) {
        int keyIndex = 0;
        int pathIndex = 0;
        while (hasNext(node, keyIndex, node.key().length(), pathIndex, path.length())) {
            if (node.key().charAt(keyIndex) == '*') {
                setWildCardParameter(path, route, node, keyIndex, pathIndex,
                        node.key().length(), path.length());
                route.handler(node.value());
                route.crumb(node.key());
                return;
            }
            if (node.key().charAt(keyIndex) == ':') {
                int endOfPath = eofPartIndex(path, pathIndex);
                int endOfKey = eofPartIndex(node.key(), keyIndex);
                setNamedParameter(path, route, node, keyIndex, pathIndex,
                        endOfKey, endOfPath);
                pathIndex = endOfPath;
                keyIndex = endOfKey;
            } else {
                keyIndex++;
                pathIndex++;
            }
        }
        if (path.length() == 1 && node.key().length() == 1 && node.value() == null) {
            findChildNode(node, "/", route);
            return;
        }
        if (path.length() == pathIndex
                || isTrailingSlash(path, node, pathIndex, path.length())) {
            route.handler(node.value());
            route.crumb(node.key());
            return;
        }
        findChildNode(node, path.substring(pathIndex, path.length()), route);
    }

    private void setWildCardParameter(String path, Route route, Node node,
                                      int keyIndex, int pathIndex, int keyLength, int pathLength) {
        String value = path.substring(pathIndex, pathLength);
        if (value.isEmpty()) {
            value = "/";
        } else {
            if (value.charAt(0) != '/') {
                value = String.format("/%s", value);
            }
        }
        route.parameters().put(node.key().substring(keyIndex, keyLength).substring(1), value);
    }

    private void setNamedParameter(String path, Route route, Node node,
                                   int keyIndex, int pathIndex, int keyLength, int pathLength) {
        route.parameters().put(node.key().substring(keyIndex, keyLength).substring(1),
                path.substring(pathIndex, pathLength));
    }

    private boolean hasNext(Node node, int keyIndex, int keyLength, int pathIndex, int pathLength) {
        if (keyIndex >= keyLength) {
            return false;
        }
        if (pathLength > pathIndex) {
            return true;
        }
        Character charAt = node.key().charAt(keyIndex);
        return charAt == ':' || charAt == '*';
    }

    private boolean isTrailingSlash(String path, Node node, int pathIndex, int pathLength) {
        if (node.key().isEmpty()) {
            return false;
        }
        if (pathLength - pathIndex != 1) {
            return false;
        }
        if (path.charAt(pathLength - 1) == '/') {
            return true;
        }
        return false;
    }

    private void findChildNode(Node node, String path, Route route) {
        List<Node> children = node.children();
        for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
            Node child = children.get(i);
            if (child.key().startsWith(String.valueOf(path.charAt(0)))
                    || child.key().charAt(0) == ':'
                    || child.key().charAt(0) == '*') {
                route.crumb(node.key());
                find(path, route, child);
                break;
            }
        }
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
}