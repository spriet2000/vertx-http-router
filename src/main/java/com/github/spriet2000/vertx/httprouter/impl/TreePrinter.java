package com.github.spriet2000.vertx.httprouter.impl;

import java.util.Formatter;

public class TreePrinter {

    public static void print(Tree tree) {
        printNode(new Formatter(System.out), 0, tree.root());
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
