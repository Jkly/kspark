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
        "spark service" {
            val client = OkHttpClient()

            get("/hello") { "Hello World!" }

            post("/hello") { "Hello World: ${request.body()}" }

            put("/item/:id") { "Added item ${request.params(":id")}: ${request.body()}" }

            should("receive get request and respond with string in body") {
                val request = Request.Builder().get().url("http://localhost:4567/hello").build()

                val response = client.newCall(request).execute()

                response.body().string() shouldBe "Hello World!"
            }

            should("receive post request body and include in response") {
                val request = Request.Builder().url("http://localhost:4567/hello")
                        .post(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()

                val response = client.newCall(request).execute()

                response.body().string() shouldBe "Hello World: Bob"
            }

            should("receive put request param and include in response") {
                val request = Request.Builder().url("http://localhost:4567/item/1234")
                        .put(RequestBody.create(MediaType.parse("application/text"), "Bob")).build()

                val response = client.newCall(request).execute()

                response.body().string() shouldBe "Added item 1234: Bob"
            }
        }
    }
}