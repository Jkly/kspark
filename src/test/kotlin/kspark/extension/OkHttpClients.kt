package kspark.extension

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.ConnectException

class OkHttpClients {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(OkHttpClients::class.java)
        const val MAX_CALL_ATTEMPTS = 3
        const val RETRY_DELAY_MILLIS = 100L
    }
}

fun OkHttpClient.call(request: Request) : Response {
    for (attempt in 1..OkHttpClients.MAX_CALL_ATTEMPTS) {
        try {
            val response = this.newCall(request).execute()
            if (response.isSuccessful || (response.code() >= 400 && response.code() < 500)) {
                return response
            }
        } catch (e: ConnectException) {
            OkHttpClients.LOGGER.error("Could not connect on attempt $attempt", e)
            if (attempt == OkHttpClients.MAX_CALL_ATTEMPTS) {
                throw e
            }
            Thread.sleep(OkHttpClients.RETRY_DELAY_MILLIS)
        }
    }
    throw IOException("Could not execute: $request")
}