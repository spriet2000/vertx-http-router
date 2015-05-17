package com.github.spriet2000.vertx.httprouter;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static com.github.spriet2000.vertx.httprouter.Router.router;


public class RouterTest  extends TestBase {

    @Test
    @Ignore
    public void test1() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Router router = router();

        router.get("/dir1/*filepath", (request, params) -> {
            Boolean x = true;
        });

        router.get("/dir2/*filepath", (request, params) -> {
            Boolean x = true;
        });

        router.get("/dir3/*filepath", (request, params) -> {
            Boolean x = true;
        });

        router.get("/*filepath", (request, params) -> {
            Boolean x = true;
        });

        server.requestHandler(router).listen(onSuccess(res -> latch.countDown()));

        get("/", req -> { }, res -> assertEquals(200, res.statusCode()));

        awaitLatch(latch);

        testComplete();
    }
}
