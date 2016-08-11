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

fun halt(status: Int, body: String) {
    Spark.halt(status, body)
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

private fun adapt(handle: KRoute.() -> Any) =
        { request: Request, response: Response ->
            handle(KRoute(request, response))
        }

