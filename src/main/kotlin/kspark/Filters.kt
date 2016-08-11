package kspark

import spark.Request
import spark.Response
import spark.Spark

fun before(path: String, routeHandler: KRoute.() -> Unit) {
    Spark.before(path, makeFilter(routeHandler))
}

fun after(path: String, routeHandler: KRoute.() -> Unit) {
    Spark.after(path, makeFilter(routeHandler))
}

fun before(routeHandler: KRoute.() -> Unit) {
    Spark.before(makeFilter(routeHandler))
}

fun after(routeHandler: KRoute.() -> Unit) {
    Spark.after(makeFilter(routeHandler))
}

fun before(path: String, acceptType: String, routeHandler: KRoute.() -> Unit) {
    Spark.before(path, acceptType, makeFilter(routeHandler))
}

fun after(path: String, acceptType: String, routeHandler: KRoute.() -> Unit) {
    Spark.after(path, acceptType, makeFilter(routeHandler))
}

private fun makeFilter(handle: KRoute.() -> Unit) =
        { request: Request, response: Response ->
            handle(KRoute(request, response))
        }
