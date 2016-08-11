package kspark

import spark.Request
import spark.Response
import spark.Spark

fun get(endpoint : String, routeHandler: KRoute.() -> Any) {
    Spark.get(endpoint, adapt(routeHandler))
}

fun post(endpoint : String, routeHandler: KRoute.() -> Any) {
    Spark.post(endpoint, adapt(routeHandler))
}

fun put(path: String, routeHandler: KRoute.() -> Any) {
    Spark.put(path, adapt(routeHandler))
}

fun patch(path: String, routeHandler: KRoute.() -> Any) {
    Spark.patch(path, adapt(routeHandler))
}

fun delete(path: String, routeHandler: KRoute.() -> Any) {
    Spark.delete(path, adapt(routeHandler))
}

fun head(path: String, routeHandler: KRoute.() -> Any) {
    Spark.head(path, adapt(routeHandler))
}

fun trace(path: String, routeHandler: KRoute.() -> Any) {
    Spark.trace(path, adapt(routeHandler))
}

fun connect(path: String, routeHandler: KRoute.() -> Any) {
    Spark.connect(path, adapt(routeHandler))
}

fun options(path: String, routeHandler: KRoute.() -> Any) {
    Spark.options(path, adapt(routeHandler))
}

fun get(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.get(path, acceptType, adapt(routeHandler))
}

fun post(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.post(path, acceptType, adapt(routeHandler))
}

fun put(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.put(path, acceptType, adapt(routeHandler))
}

fun patch(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.patch(path, acceptType, adapt(routeHandler))
}

fun delete(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.delete(path, acceptType, adapt(routeHandler))
}

fun head(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.head(path, acceptType, adapt(routeHandler))
}

fun trace(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.trace(path, acceptType, adapt(routeHandler))
}

fun connect(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.connect(path, acceptType, adapt(routeHandler))
}

fun options(path: String, acceptType: String, routeHandler: KRoute.() -> Any) {
    Spark.options(path, acceptType, adapt(routeHandler))
}

private fun adapt(handle: KRoute.() -> Any) =
        { request: Request, response: Response ->
            handle(KRoute(request, response))
        }

