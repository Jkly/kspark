package kspark

import io.kotlintest.specs.ShouldSpec
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import spark.Spark

class KSparkIntegrationTest : ShouldSpec() {
    override val oneInstancePerTest = true

    override fun afterAll() {
        Spark.stop()
    }

    init {
        val client = OkHttpClient()

        "spark service" {
            get("/hello") { "Hello World!" }

            post("/hello") { "Hello World: " + request.body() }

            should("receive get request and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:4567/hello").build()

                val response = client.newCall(request).execute()

                response.isSuccessful shouldBe true
                response.body().string() shouldBe "Hello World!"
            }

            should("receive post request body and include in response") {
                val request = Request.Builder().url("http://localhost:4567/hello")
                        .post(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()

                val response = client.newCall(request).execute()

                response.isSuccessful shouldBe true
                response.body().string() shouldBe "Hello World: Bob"
            }
        }
    }
}