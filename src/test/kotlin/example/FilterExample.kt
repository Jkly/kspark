package example

import kspark.*
import spark.Spark.*

fun main(args: Array<String>) {
    get("/hello") { "Hello World!" }

    before {
        if (request.headers("user") != "authenticated user") {
            halt(401, "You are not welcome here")
        }
    }

    after {
        response.header("foo", "set by after filter")
    }
}
