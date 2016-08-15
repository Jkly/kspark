package kspark

import io.kotlintest.specs.ShouldSpec
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Spark
import java.io.IOException
import java.net.ConnectException

class KSparkIntegrationTest : ShouldSpec() {
    override val oneInstancePerTest = true

    val client:OkHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true).build()

    override fun afterAll() {
        Spark.stop()
    }

    init {
        "spark service" {
            get("/hello") { "Hello World!" }
            post("/hello") { "Hello World: ${request.body()}" }
            put("/item/:id") { "Added item ${request.params(":id")}: ${request.body()}" }
            patch("/item/:id") { "Patched item ${request.params(":id")}: ${request.body()}" }
            delete("/item/:id") { "Deleted item ${request.params(":id")}" }
            head("/hello") {  }
            options("/hello") { "GET,POST,HEAD,OPTIONS" }
            get("/hello", "application/json") { "{\"Hello\": \"World!\"}" }

            should("receive get request and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:4567/hello").build()
                call(request).body().string() shouldBe "Hello World!"
            }

            should("receive post request body and include in response") {
                val request = Request.Builder().url("http://localhost:4567/hello")
                        .post(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()
                call(request).body().string() shouldBe "Hello World: Bob"
            }

            should("receive put request param and include in response") {
                val request = Request.Builder().url("http://localhost:4567/item/1234")
                        .put(RequestBody.create(MediaType.parse("application/json"), "{\"id\": 1234, \"name\": \"Bob\"}")).build()
                call(request).body().string() shouldBe "Added item 1234: {\"id\": 1234, \"name\": \"Bob\"}"
            }

            should("receive patch request param and include in response") {
                val request = Request.Builder().url("http://localhost:4567/item/1234")
                        .patch(RequestBody.create(MediaType.parse("application/json"), "{\"name\": \"Bob\"}")).build()
                call(request).body().string() shouldBe "Patched item 1234: {\"name\": \"Bob\"}"
            }

            should("receive delete request param") {
                val request = Request.Builder().url("http://localhost:4567/item/1234").delete().build()
                call(request).body().string() shouldBe "Deleted item 1234"
            }

            should("receive head request and respond with nothing") {
                val request = Request.Builder().url("http://localhost:4567/hello").head().build()
                call(request).body().string() shouldBe ""
            }

            should("receive options request and respond with valid verbs") {
                val request = Request.Builder().url("http://localhost:4567/hello").method("OPTIONS", null).build()
                call(request).body().string() shouldBe "GET,POST,HEAD,OPTIONS"
            }

            should("receive get request with json media type and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:4567/hello")
                        .addHeader("Accept", "application/json").build()
                call(request).body().string() shouldBe "{\"Hello\": \"World!\"}"
            }
        }
    }

    private fun call(request: Request) : Response {
        for (attempt in 1..MAX_CALL_ATTEMPTS) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: ConnectException) {
                LOGGER.error("Could not connect on attempt $attempt", e)
                if (attempt == MAX_CALL_ATTEMPTS) {
                    throw e
                }
                Thread.sleep(RETRY_DELAY_MILLIS)
            }
        }
        throw IOException("Could not execute: $request")
    }

    companion object {
        val LOGGER:Logger = LoggerFactory.getLogger(KSparkIntegrationTest::class.java)
        const val MAX_CALL_ATTEMPTS = 3
        const val RETRY_DELAY_MILLIS = 100L
    }

}