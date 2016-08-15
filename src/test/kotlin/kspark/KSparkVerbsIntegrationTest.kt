package kspark

import io.kotlintest.specs.ShouldSpec
import kspark.extension.call
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import spark.Spark

class KSparkVerbsIntegrationTest : ShouldSpec() {
    val client:OkHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true).build()

    override fun afterAll() {
        Spark.stop()
    }

    init {
        "spark service" {
            Spark.port(56789)

            get("/hello") { "Hello World!" }
            post("/hello") { "Hello World: ${request.body()}" }
            put("/item/:id") { "Added item ${request.params(":id")}: ${request.body()}" }
            patch("/item/:id") { "Patched item ${request.params(":id")}: ${request.body()}" }
            delete("/item/:id") { "Deleted item ${request.params(":id")}" }
            head("/hello") {  }
            options("/hello") { "GET,POST,HEAD,OPTIONS" }

            get("/hello", "application/json") { "{\"Hello\": \"World!\"}" }
            post("/hello", "application/json") { "{\"requestBody\": \"${request.body()}\"}" }
            put("/item/:id", "application/json") { "{\"addedItem\": \"${request.params(":id")}\"}" }
            patch("/item/:id", "application/json") { "{\"patchedItem\": \"${request.params(":id")}\"}" }
            delete("/item/:id", "application/json") { "{\"deletedItem\": \"${request.params(":id")}\"}" }
            options("/hello", "application/json") { "{\"options\": [\"GET\",\"POST\",\"HEAD\",\"OPTIONS\"}" }

            should("receive get request and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:56789/hello").build()
                client.call(request).body().string() shouldBe "Hello World!"
            }

            should("receive post request body and include in response") {
                val request = Request.Builder().url("http://localhost:56789/hello")
                        .post(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()
                client.call(request).body().string() shouldBe "Hello World: Bob"
            }

            should("receive put request param and include in response") {
                val request = Request.Builder().url("http://localhost:56789/item/1234")
                        .put(RequestBody.create(MediaType.parse("application/json"), "{\"id\": 1234, \"name\": \"Bob\"}")).build()
                client.call(request).body().string() shouldBe "Added item 1234: {\"id\": 1234, \"name\": \"Bob\"}"
            }

            should("receive patch request param and include in response") {
                val request = Request.Builder().url("http://localhost:56789/item/1234")
                        .patch(RequestBody.create(MediaType.parse("application/json"), "{\"name\": \"Bob\"}")).build()
                client.call(request).body().string() shouldBe "Patched item 1234: {\"name\": \"Bob\"}"
            }

            should("receive delete request param") {
                val request = Request.Builder().url("http://localhost:56789/item/1234").delete().build()
                client.call(request).body().string() shouldBe "Deleted item 1234"
            }

            should("receive head request and respond with nothing") {
                val request = Request.Builder().url("http://localhost:56789/hello").head().build()
                client.call(request).body().string() shouldBe ""
            }

            should("receive options request and respond with valid verbs") {
                val request = Request.Builder().url("http://localhost:56789/hello").method("OPTIONS", null).build()
                client.call(request).body().string() shouldBe "GET,POST,HEAD,OPTIONS"
            }

            should("receive get request with json accept type and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:56789/hello")
                        .addHeader("Accept", "application/json").build()
                client.call(request).body().string() shouldBe "{\"Hello\": \"World!\"}"
            }

            should("receive post request with json accept type and include in body") {
                val request = Request.Builder().get().url("http://localhost:56789/hello")
                        .addHeader("Accept", "application/json")
                        .post(RequestBody.create(MediaType.parse("application/text"), "Bob"))
                        .build()
                client.call(request).body().string() shouldBe "{\"requestBody\": \"Bob\"}"
            }

            should("receive put request param with json accept type and include in response") {
                val request = Request.Builder().url("http://localhost:56789/item/1234")
                        .addHeader("Accept", "application/json")
                        .put(RequestBody.create(MediaType.parse("application/json"), "{\"id\": 1234, \"name\": \"Bob\"}")).build()
                client.call(request).body().string() shouldBe "{\"addedItem\": \"1234\"}"
            }

            should("receive patch request param with json accept type and include in response") {
                val request = Request.Builder().url("http://localhost:56789/item/1234")
                        .addHeader("Accept", "application/json")
                        .patch(RequestBody.create(MediaType.parse("application/json"), "{\"name\": \"Bob\"}")).build()
                client.call(request).body().string() shouldBe "{\"patchedItem\": \"1234\"}"
            }

            should("receive delete request param with json accept type") {
                val request = Request.Builder().url("http://localhost:56789/item/1234")
                        .addHeader("Accept", "application/json")
                        .delete().build()
                client.call(request).body().string() shouldBe "{\"deletedItem\": \"1234\"}"
            }

            should("receive options request with json accept type and respond with valid verbs") {
                val request = Request.Builder().url("http://localhost:56789/hello")
                        .addHeader("Accept", "application/json")
                        .method("OPTIONS", null).build()
                client.call(request).body().string() shouldBe "{\"options\": [\"GET\",\"POST\",\"HEAD\",\"OPTIONS\"}"
            }
        }
    }
}