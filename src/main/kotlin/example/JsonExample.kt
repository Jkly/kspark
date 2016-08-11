package example

import kspark.*
import kspark.extension.fromJson
import kspark.extension.toJson

data class FooBar(val foo:String, val bar:String)

fun main(args: Array<String>) {
    get("/example.json") {
        response.type("application/json")
        FooBar("foo", "bar").toJson()
    }

    post("/example.json") {
        val fooBar:FooBar = request.body().fromJson()
        "got ${fooBar.foo} ${fooBar.bar}"
    }
}
