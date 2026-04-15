package org.coinroutine.project.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(engine: HttpClientEngine) : HttpClient{
        return HttpClient(engine){
            //This plugin automatically manages the serialization and deserialization of request and response body
            install(ContentNegotiation){
                json(
                    json = Json{
                        ignoreUnknownKeys=true
                    }
                )
            }
            //Second,install for HTTP timeouts.
            //This plugin lets us specify timeouts for our requests.
            install(HttpTimeout){
                socketTimeoutMillis= 20_000L
                requestTimeoutMillis= 20_000L
            }
            //used for caching
            install(HttpCache)

            //the default request extension function to set default headers for every request
            defaultRequest {
                headers{
                    append("x-access-token", "lookingForTheApi-insertHereToken")
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}