package com.github.spriet2000.vertx.httprouter;

import io.vertx.core.http.*;
import io.vertx.test.core.HttpTestBase;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class TestBase extends HttpTestBase {
    @Override
    public void setUp() throws Exception {
        super.setUp();

        server = vertx.createHttpServer(new HttpServerOptions()
                .setPort(8080).setHost("localhost"));

        client = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultPort(8080));
    }

    @Override
    public void tearDown() throws Exception {
        if (client != null) {
            client.close();
        }
        if (server != null) {
            CountDownLatch latch = new CountDownLatch(1);
            server.close((asyncResult) -> {
                assertTrue(asyncResult.succeeded());
                latch.countDown();
            });
            awaitLatch(latch);
        }
        super.tearDown();
    }

    protected void post(String path, Consumer<HttpClientRequest> requestAction, Consumer<HttpClientResponse> responseAction) throws InterruptedException {
        testRequestBuffer(HttpMethod.POST, 8080, path, requestAction, responseAction);
    }

    protected void post(String path, Consumer<HttpClientRequest> requestAction) throws InterruptedException {
        testRequestBuffer(HttpMethod.POST, 8080, path, requestAction, null);
    }

    protected void get(String path, Consumer<HttpClientRequest> requestAction, Consumer<HttpClientResponse> responseAction) throws InterruptedException {
        testRequestBuffer(HttpMethod.GET, 8080, path, requestAction, responseAction);
    }

    protected void get(String path, Consumer<HttpClientRequest> requestAction) throws InterruptedException {
        testRequestBuffer(HttpMethod.GET, 8080, path, requestAction, null);
    }

    protected void testRequestBuffer(HttpMethod method, int port, String path, Consumer<HttpClientRequest> requestAction,
                                     Consumer<HttpClientResponse> responseAction) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        HttpClientRequest req = client.request(method, port, "localhost", path, resp -> {
            if (responseAction != null) {
                responseAction.accept(resp);
            }

        });
        if (requestAction != null) {
            requestAction.accept(req);
        }
        req.end();
        awaitLatch(latch);
    }
}
