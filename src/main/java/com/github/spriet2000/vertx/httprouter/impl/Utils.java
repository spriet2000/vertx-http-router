package com.github.spriet2000.vertx.httprouter.impl;

import java.util.Formatter;
import java.util.Objects;

public class Utils {
    static int eofPartIndex(String path, int start) {
        int index;
        for (index = start; index < path.length(); index++) {
            if (path.charAt(index) == '/') {
                return index;
            }
        }
        return index;
    }

    static String sanitizePath(String path) {
        return clean(path).toLowerCase();
    }

    public static void printTree(Tree tree) {
        printNode(new Formatter(System.out), 0, tree.root());
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

    private static void printNode(Formatter f, int level, Node node) {
        for (int i = 0; i < level; i++) {
            f.format(" ");
        }
        f.format("|");
        for (int i = 0; i < level; i++) {
            f.format("-");
        }

        f.format("%s (%s, %s)%n", node.key(), node.priority(), node.value());

        for (Node child : node.children()) {
            printNode(f, level + 1, child);
        }
    }
}
