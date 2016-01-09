package com.github.spriet2000.vertx.httprouter;

import com.github.spriet2000.vertx.httprouter.impl.Tree;
import com.github.spriet2000.vertx.httprouter.impl.TreePrinter;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TreeTest extends VertxTestBase {

    @Test
    public void testRoot() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/dir1/*filepath1", handler)
                .addNode("/dir2/*filepath2", handler)
                .addNode("/*filepath3", handler);

        TreePrinter.print(tree);

        testRoute(tree, "/", true, "/*filepath3", params("*filepath3", "/"));
    }

    @Test
    public void testTrailingSlashes1() {
        Tree tree = new Tree()
                .addNode("/search/", (request, parameters) -> {
                });
        testRoute(tree, "/search", true, "/search/", null);
        testComplete();
    }

    @Test
    public void testTrailingSlashes2() {
        Tree tree = new Tree()
                .addNode("/search", (request, parameters) -> {
                });
        testRoute(tree, "/search/", true, "/search", null);
        testComplete();
    }

    @Test
    public void testSet1() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        TreePrinter.print(tree);

        testRoute(tree, "/", true, "/", null);
        testRoute(tree, "/cmd/test/", true, "/cmd/:tool/", params(":tool", "test"));
        testRoute(tree, "/cmd/test", true, "/cmd/:tool/", params(":tool", "test"));
        testRoute(tree, "/cmd/test/3", true, "/cmd/:tool/:sub", params(":tool", "test", ":sub", "3"));
        testRoute(tree, "/src/", true, "/src/*filepath", params("*filepath", "/"));
        testRoute(tree, "/src/some/file.png", true, "/src/*filepath", params("*filepath", "/some/file.png"));
        testRoute(tree, "/search/", true, "/search/", null);
        testRoute(tree, "/search/someth!ng+in+ünìcodé", true, "/search/:query", params(":query", "someth!ng+in+ünìcodé"));
        testRoute(tree, "/search/someth!ng+in+ünìcodé/", true, "/search/:query", params(":query", "someth!ng+in+ünìcodé"));
        testRoute(tree, "/user_gopher", true, "/user_:name", params(":name", "gopher"));
        testRoute(tree, "/user_gopher/about", true, "/user_:name/about", params(":name", "gopher"));
        testRoute(tree, "/files/js/inc/framework.js", true, "/files/:dir/*filepath",
                params(":dir", "js", "*filepath", "/inc/framework.js"));
        testRoute(tree, "/info/gordon/public", true, "/info/:user/public", params(":user", "gordon"));
        testRoute(tree, "/info/gordon/project/java", true, "/info/:user/project/:project",
                params(":user", "gordon", ":project", "java"));

        testComplete();
    }

    @Test
    public void testCase1() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        TreePrinter.print(tree);

        // whats so special about this??
        testRoute(tree, "/src/", true, "/src/*filepath", params("*filepath", "/"));
    }

    @Test
    public void testCase2() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        TreePrinter.print(tree);

        // whats so special about this??
        testRoute(tree, "/cmd/test", true, "/cmd/:tool/", params(":tool", "test"));
    }

    @Test
    public void testCase3() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        TreePrinter.print(tree);

        // whats so special about this??
        testRoute(tree, "/cmd/test/3", true, "/cmd/:tool/:sub", params(":tool", "test", ":sub", "3"));
    }

    @Test
    public void testCase4() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        TreePrinter.print(tree);

        // whats so special about this??
        testRoute(tree, "/search/someth!ng+in+ünìcodé/", true, "/search/:query", params(":query", "someth!ng+in+ünìcodé"));
    }

    @Test
    public void testCase5() {
        RouteHandler handler = (request, parameters) -> {
        };

        Tree tree = new Tree()
                .addNode("/", handler)
                .addNode("/cmd/:tool/:sub", handler)
                .addNode("/cmd/:tool/", handler)
                .addNode("/src/*filepath", handler)
                .addNode("/search/", handler)
                .addNode("/search/:query", handler)
                .addNode("/user_:name", handler)
                .addNode("/user_:name/about", handler)
                .addNode("/files/:dir/*filepath", handler)
                .addNode("/doc/", handler)
                .addNode("/doc/go_faq.html", handler)
                .addNode("/doc/go1.html", handler)
                .addNode("/info/:user/public", handler)
                .addNode("/info/:user/project/:project", handler);

        TreePrinter.print(tree);

        // whats so special about this??
        testRoute(tree, "/", true, "/", null);
    }

    @Test
    public void testPriority() {
        Tree tree = new Tree()
                .addNode("/dir/*filepath", (request, parameters) -> {
                })
                .addNode("/*filepath", (request, parameters) -> {
                });

        testRoute(tree, "/dir/file1", true, "/dir/*filepath", params("*filepath", "/file1"));
        testRoute(tree, "/file1", true, "/*filepath", params("*filepath", "/file1"));

    }

    @Test
    public void testPriority1() {
        AtomicInteger integer = new AtomicInteger();
        Tree tree = new Tree()
                .addNode("/dir1/*filepath1", (request, parameters) -> integer.set(0))
                .addNode("/dir2/*filepath2", (request, parameters) -> integer.set(1))
                .addNode("/*filepath3", (request, parameters) -> integer.set(2));

        TreePrinter.print(tree);

        Route route = tree.find("/");

        route.handler().handle(null, null);

        assertEquals(2, integer.intValue());
    }

    private void testRoute(Tree tree, String path, boolean handler, String trail, Map<String, String> params) {
        Route route = tree.find(path, true);
        assertEquals(trail, route.trail());
        if (handler) {
            assertNotNull(route.handler());
        } else {
            assertNull(route.handler());
        }
        if (params != null) {
            assertEquals(params.size(), route.parameters().size());
            route.parameters().forEach((k, v) -> assertEquals(params.get(k), v));
        } else {
            assertEquals(0, route.parameters().size());
        }
    }

    private Map<String, String> params(String... strings) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0, l = strings.length; i < l; i = i + 2) {
            map.put(strings[i], strings[i + 1]);
        }
        return map;
    }
}
