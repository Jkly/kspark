package example

import kspark.*

fun main(args: Array<String>) {
    get("/hello") { "Hello World!" }

    post("/hello") { "Hello World: " + request.body() }

    get("/private") {
        response.status(401)
        "Go Away!!!"
    }

    get("/users/:name") { "Selected user: " + request.params(":name") }

    get("/news/:section") {
        response.type("text/xml")
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>"
    }

    get("/protected") {
        halt(403, "I don't think so!!!")
    }

    get("/redirect") {
        response.redirect("/news/world")
    }

    get("/") { "root" }
}
