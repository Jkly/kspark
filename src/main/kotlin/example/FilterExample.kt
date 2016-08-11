package example

import kspark.after
import kspark.before
import kspark.get
import spark.Spark.halt

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
