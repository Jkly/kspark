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

private fun adapt(handle: KRoute.() -> Any) =
        { request: Request, response: Response ->
            handle(KRoute(request, response))
        }

