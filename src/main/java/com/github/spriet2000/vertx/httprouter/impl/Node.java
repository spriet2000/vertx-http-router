package com.github.spriet2000.vertx.httprouter.impl;

import com.github.spriet2000.vertx.httprouter.RouteHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Node {
    private List<Node> children = new ArrayList<>();

    private String key = "";
    private RouteHandler value;
    private int priority = 0;

    public RouteHandler value() {
        return value;
    }

    public void value(RouteHandler handler) {
        this.value = handler;
    }

    public String key() {
        return key;
    }

    public void key(String value) {
        this.key = value;
        this.priority = priority(key);
    }

    public List<Node> children() {
        return children;
    }

    public void children(List<Node> children) {
        this.children = children;
    }

    public int priority() {
        return priority;
    }

    private int priority(String key) {
        int matchingChars = 0;
        int weight = 1;
        while (matchingChars < key.length()) {
            if (key.charAt(matchingChars) == '*') {
                return 0;
            }
            if (key.charAt(matchingChars) == ':') {
                return 1;
            }
            matchingChars++;
            weight++;
        }
        return weight;
    }

    public void sort() {
        children = children.stream()
                .sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority()))
                .collect(Collectors.toList());
    }
}
