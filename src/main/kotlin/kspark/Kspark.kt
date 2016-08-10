package kspark

import spark.Request
import spark.Response
import spark.Spark

fun get(endpoint : String, route: KRoute.() -> Any) {
    Spark.get(endpoint, adapt(route))
}

fun post(endpoint : String, route: KRoute.() -> Any) {
    Spark.post(endpoint, adapt(route))
}

fun halt(status: Int, body: String) {
    Spark.halt(status, body)
}

private fun adapt(handle: KRoute.() -> Any) =
        { request: Request, response: Response ->
            handle(KRoute(request, response))
        }

