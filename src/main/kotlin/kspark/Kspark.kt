package kspark

import spark.Request
import spark.Response
import spark.Spark

fun get(endpoint : String,
        route: RouteHandler.() -> Any) {
    Spark.get(endpoint, adapt(route))
}

fun post(endpoint : String,
        route: RouteHandler.() -> Any) {
    Spark.post(endpoint, adapt(route))
}

fun halt(status: Int, body: String) {
    Spark.halt(status, body)
}

private fun adapt(handler: RouteHandler.() -> Any) =
        { request: Request, response: Response ->
            handler.invoke(RouteHandler(request, response))
        }


data class RouteHandler(val request: Request, val response: Response)