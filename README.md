# kspark
A Kotlin DSL for [Spark](https://github.com/perwendel/spark) - web development framework.

```kotlin
import kspark.*

fun main(args: Array<String>) {
    get("/hello") {
        "Hello World!"
    }
}
```
View at: `http://localhost:4567/hello`

## Examples

Create endpoints with less code:
```kotlin
import kspark.*

fun main(args: Array<String>) {
    get("/hello") { 
        "Hello World!" 
    }

    post("/hello") { 
        "Hello World: " + request.body() 
    }

    get("/private") {
        response.status(401)
        "Go Away!!!"
    }

    get("/users/:name") { 
        "Selected user: " + request.params(":name") 
    }

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

    get("/") { 
        "root" 
    }
}
```
---
Respond with JSON:
```kotlin
import kspark.*
import kspark.extension.toJson

data class FooBar(val foo:String, val bar:String)

fun main(args: Array<String>) {
    get("/example.json") {
        response.type("application/json")
        FooBar("foo", "bar").toJson()
    }
}
```
