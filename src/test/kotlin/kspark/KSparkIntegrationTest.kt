package kspark

import io.kotlintest.specs.ShouldSpec
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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

            should("receive get request and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:4567/hello").build()

                call(request) shouldBe "Hello World!"
            }

            should("receive post request body and include in response") {
                val request = Request.Builder().url("http://localhost:4567/hello")
                        .post(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()

                call(request) shouldBe "Hello World: Bob"
            }

            should("receive put request param and include in response") {
                val request = Request.Builder().url("http://localhost:4567/item/1234")
                        .put(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()

                call(request) shouldBe "Added item 1234: Bob"
            }
        }
    }

    private fun call(request: Request) : String {
        for (attempt in 1..MAX_CALL_ATTEMPTS) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return response.body().string()
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