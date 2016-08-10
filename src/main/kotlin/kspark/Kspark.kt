package kspark

import spark.Request
import spark.Response
import spark.Route
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

private fun adapt(handler: RouteHandler.() -> Any) = RouteAdaptor(handler)

private class RouteAdaptor(val handler: RouteHandler.() -> Any) : Route {
    override fun handle(request: Request?, response: Response?): Any {
        return handler.invoke(RouteHandler(request!!, response!!))
    }
}

data class RouteHandler(val request: Request, val response: Response)