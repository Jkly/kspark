package kspark

import spark.Request
import spark.Response

data class KRoute(val request: Request, val response: Response)