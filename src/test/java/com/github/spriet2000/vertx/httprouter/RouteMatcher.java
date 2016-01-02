package com.github.spriet2000.vertx.httprouter;


import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteMatcher {

    private final List<PatternBinding> bindings = new ArrayList<>();

    public RouteMatcher() {
    }

    public Route find(String path) {
        return route(path, bindings);
    }

    public RouteMatcher add(String pattern, Handler<HttpServerRequest> handler) {
        addPattern(pattern, handler);
        return this;
    }

    private void addPattern(String input, Handler<HttpServerRequest> handler) {
        // We need to search for any :<token name> tokens in the String and replace them with named capture groups
        Matcher m =  Pattern.compile(":([A-Za-z][A-Za-z0-9_]*)").matcher(input);
        StringBuffer sb = new StringBuffer();
        Set<String> groups = new HashSet<>();
        while (m.find()) {
            String group = m.group().substring(1);
            if (groups.contains(group)) {
                throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
            }
            m.appendReplacement(sb, "(?<$1>[^\\/]+)");
            groups.add(group);
        }
        m.appendTail(sb);
        String regex = sb.toString();
        PatternBinding binding = new PatternBinding(Pattern.compile(regex), groups, handler);
        bindings.add(binding);
    }

    private Route route(String path, List<PatternBinding> bindings) {
        for (PatternBinding binding: bindings) {
            Matcher m = binding.pattern.matcher(path);
            if (m.matches()) {
                Map<String, String> params = new HashMap<>(m.groupCount());
                if (binding.paramNames != null) {
                    // Named params
                    for (String param: binding.paramNames) {
                        params.put(param, m.group(param));
                    }
                } else {
                    // Un-named params
                    for (int i = 0; i < m.groupCount(); i++) {
                        params.put("param" + i, m.group(i + 1));
                    }
                }
                return new Route(params, path);
            }
        }
        return null;
    }

    private static class PatternBinding {
        final Pattern pattern;
        final Handler<HttpServerRequest> handler;
        final Set<String> paramNames;

        private PatternBinding(Pattern pattern, Set<String> paramNames, Handler<HttpServerRequest> handler) {
            this.pattern = pattern;
            this.paramNames = paramNames;
            this.handler = handler;
        }
    }

    public class Route{
        private Map<String, String> params;
        private String path;

        public Route(Map<String, String> params, String path){

            this.params = params;
            this.path = path;
        }


        public Map<String, String> getParams() {
            return params;
        }

        public String getPath() {
            return path;
        }
    }
}