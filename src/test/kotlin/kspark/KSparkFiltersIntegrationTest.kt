package kspark

import io.kotlintest.specs.ShouldSpec
import kspark.extension.call
import okhttp3.OkHttpClient
import okhttp3.Request
import spark.Spark

class KSparkFiltersIntegrationTest : ShouldSpec() {
    val client:OkHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true).build()

    override fun afterAll() {
        Spark.stop()
    }

    init {
        Spark.port(PORT)
        
        "spark service" {
            get("/hello") { "Hello World!" }
            get("/hello", "application/json") { "{\"Hello\": \"World!\"}" }
            before {
                if (request.headers("user") != "authenticated user") {
                    Spark.halt(401, "You are not welcome here")
                }
            }
            after {
                response.header("foo", "set by after filter")
            }

            get("/accept-check") { "{\"Hello\": \"World!\"}" }
            before("/accept-check", "application/xml") {
                Spark.halt(406, "xml not supported")
            }

            should("receive get request and filter halts it") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello").build()
                val response = client.call(request)
                response.code() shouldBe 401
                response.body().string() shouldBe "You are not welcome here"
            }

            should("receive get request and not filter with the authentication header") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello")
                        .addHeader("user", "authenticated user")
                        .build()
                val response = client.call(request)
                response.body().string() shouldBe "Hello World!"
            }

            should("receive get request and receive header added by after filter") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello")
                        .addHeader("user", "authenticated user")
                        .build()
                val response = client.call(request)
                response.header("foo") shouldBe "set by after filter"
            }

            should("receive get request and filter by accept type") {
                val request = Request.Builder().get().url("http://localhost:$PORT/accept-check")
                        .addHeader("user", "authenticated user")
                        .addHeader("Accept", "application/xml").build()
                val response = client.call(request)
                response.code() shouldBe 406
                response.body().string() shouldBe "xml not supported"
            }

            should("receive get request and not go through filter with accept type") {
                val request = Request.Builder().get().url("http://localhost:$PORT/accept-check")
                        .addHeader("user", "authenticated user")
                        .addHeader("Accept", "application/json").build()
                val response = client.call(request)
                response.code() shouldBe 200
            }

        }
    }

    companion object {
        const val PORT = 5678
    }
}