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
            get("/hello-typed", "application/json") { "{\"Hello\": \"World!\"}" }
            get("/hello-typed", "application/xml") { "<Hello>World!</Hello>" }

            get("/authenticated") { "authed" }

            before("/hello-typed", "application/audio") {
                Spark.halt(406, "audio not supported")
            }

            before("/authenticated") {
                if (request.headers("user") != "authenticated user") {
                    Spark.halt(401, "You are not welcome here")
                }
            }
            after {
                response.header("foo", "set by after filter")
            }

            after("/*", "application/xml") {
                response.header("Content-type", "application/xml")
            }
            after("/*", "application/json") {
                response.header("Content-type", "application/json")
            }

            should("receive get request and filter halts it") {
                val request = Request.Builder().get().url("http://localhost:$PORT/authenticated").build()
                val response = client.call(request)
                response.code() shouldBe 401
                response.body().string() shouldBe "You are not welcome here"
            }

            should("receive get request and not filter with the authentication header") {
                val request = Request.Builder().get().url("http://localhost:$PORT/authenticated")
                        .addHeader("user", "authenticated user")
                        .build()
                val response = client.call(request)
                response.body().string() shouldBe "authed"
            }

            should("receive get request and receive header added by after filter") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello")
                        .build()
                val response = client.call(request)
                response.header("foo") shouldBe "set by after filter"
            }

            should("receive get request and filter by accept type") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello-typed")
                        .addHeader("Accept", "application/audio").build()
                val response = client.call(request)
                response.code() shouldBe 406
                response.body().string() shouldBe "audio not supported"
            }

            should("receive get request and set the json content-type in the response header") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello-typed")
                        .addHeader("Accept", "application/json").build()
                val response = client.call(request)
                response.header("Content-Type") shouldBe "application/json"
                response.body().string() shouldBe "{\"Hello\": \"World!\"}"
            }

            should("receive get request and set the xml content-type in the response header") {
                val request = Request.Builder().get().url("http://localhost:$PORT/hello-typed")
                        .addHeader("Accept", "application/xml").build()
                val response = client.call(request)
                response.header("Content-Type") shouldBe "application/xml"
                response.body().string() shouldBe "<Hello>World!</Hello>"
            }

        }
    }

    companion object {
        const val PORT = 5678
    }
}