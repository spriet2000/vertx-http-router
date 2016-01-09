# Vert.x http-router

[![Build Status](https://travis-ci.org/spriet2000/vertx-http-router.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-http-router)

## Performance

Throughput in operations per second

RouteMatcher  | Vert.x http-router
------------- | -------------
164305.285    | 611959.589

You can measure it on your own machine by using the BenchmarkRunner. 
The http-router should be at least 3 times faster than a regex based RouteMatcher.

Details about the comparison can be found [![here](https://github.com/spriet2000/vertx-http-router/blob/master/src/test/java/com/github/spriet2000/vertx/httprouter/BenchmarkCompare.java).

## Example

```java

    RouteHandler printUser = (req, params) -> req.
            response().end(params.get(":action") + " " + params.get(":user"));

    RouteHandler printPath = (req, params) -> req.
            response().end("filepath " + params.get("*filepath"));

    Router router = router()
            .get("/api/:user/:action", printUser)
            .get("/*filepath", printPath);

    server.requestHandler(router)
            .listen();

```

## Current state

* make it feature complete
* make it stable
* analyze and improve performance

## Goals for this router:

* simple and lightweight
* easy to integrate
* high performance

## Installation

### Maven

```xml

    <dependency>
        <groupId>com.github.spriet2000</groupId>
        <artifactId>vertx-http-router</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>

```

### Without maven

[https://oss.sonatype.org/content/repositories/snapshots/com/github/spriet2000](https://oss.sonatype.org/content/repositories/snapshots/com/github/spriet2000)

### Thanks

Thanks to Tahseen Ur Rehman, Javid Jamae and Dennis Heidsiek for their clear implementation of a radix tree in java which helped me getting started.

And many thanks to Julien Schmidt for his excellent [httprouter](https://github.com/julienschmidt/httprouter)!