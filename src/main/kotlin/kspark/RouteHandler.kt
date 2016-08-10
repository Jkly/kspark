package kspark

import spark.Request
import spark.Response

data class RouteHandler(val request: Request, val response: Response)