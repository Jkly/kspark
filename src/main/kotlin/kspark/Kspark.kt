package kspark

import spark.Request
import spark.Response
import spark.Spark

fun get(endpoint : String,
        routeHandler: RouteHandler.() -> Any) {
    Spark.get(endpoint, adapt(routeHandler))
}

fun post(endpoint : String,
         routeHandler: RouteHandler.() -> Any) {
    Spark.post(endpoint, adapt(routeHandler))
}

fun halt(status: Int, body: String) {
    Spark.halt(status, body)
}

private fun adapt(handler: RouteHandler.() -> Any) =
        { request: Request, response: Response ->
            handler.invoke(RouteHandler(request, response))
        }


data class RouteHandler(val request: Request, val response: Response)