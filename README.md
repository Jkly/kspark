# kspark
[![Build Status](https://semaphoreci.com/api/v1/jkly/kspark/branches/master/badge.svg)](https://semaphoreci.com/jkly/kspark) [![Release](https://jitpack.io/v/jkly/kspark.svg)](https://jitpack.io/#jkly/kspark)

A Kotlin DSL for [Spark](https://github.com/perwendel/spark), the [Ruby Sinatra](http://www.sinatrarb.com/)-inspired web development framework.

```kotlin
import kspark.*

fun main(args: Array<String>) {
    get("/hello") {
        "Hello World!"
    }
}
```
View at: `http://localhost:4567/hello`

## Aims
This is just a thin-layer over Spark to make web development easier where the Kotlin language makes it possible. 

For more information on the capabilities of the web framework, see here: http://sparkjava.com/documentation.html

## Examples

Create endpoints with less code:
```kotlin
import kspark.*

fun main(args: Array<String>) {
    get("/hello") { 
        "Hello World!" 
    }

    post("/hello") { 
        "Hello World: ${request.body()}"
    }

    get("/private") {
        response.status(401)
        "Go Away!!!"
    }

    get("/users/:name") { 
        "Selected user: ${request.params(":name")}"
    }

    get("/news/:section") {
        response.type("text/xml")
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>${request.params("section")}</news>"
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
Receiving JSON:
```kotlin
import kspark.*

data class FooBar(val foo:String, val bar:String)

fun main(args: Array<String>) {
    post("/example.json") {
        val fooBar:FooBar = request.body().fromJson()
        "got ${fooBar.foo} ${fooBar.bar}"
    }
}
```
With this request:
```json
{
  "foo": "hello",
  "bar": "world"
}
```
Returns: `got hello world`

---
Respond with JSON:
```kotlin
import kspark.*

data class FooBar(val foo:String, val bar:String)

fun main(args: Array<String>) {
    get("/example.json") {
        response.type("application/json")
        FooBar("foo", "bar").toJson()
    }
}
```
Returns:
```json
{
  "foo": "foo",
  "bar": "bar"
}
```
---
Before and after requests:
```kotlin
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
```
