package com.github.assertion.async.http

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.response.readText
import java.text.DateFormat

suspend fun main(args: Array<String>) {
    val httpClient = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
                disableDefaultTyping()
                findAndRegisterModules()
            }
        }
    }
    httpClient.use { client ->
        try {
            val pong = client.get<Pong>("http://localhost:8080/ping")
            println(pong)
        } catch (e: BadResponseStatusException) {
            println("Error ${e.statusCode}")
            e.response.receive<Error>()
            println(e.response.readText(Charsets.UTF_8))
        }
    }

}