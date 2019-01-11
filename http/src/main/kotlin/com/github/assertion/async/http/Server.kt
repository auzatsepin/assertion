package com.github.assertion.async.http

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat
import java.time.Instant

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
                disableDefaultTyping()
                findAndRegisterModules()
            }
        }
        install(ShutDownUrl.ApplicationCallFeature) {
            shutDownUrl = "/shutdown"
            exitCodeSupplier = { 0 }
        }
        routing {
            get("/ping/{id?}") {
                call.parameters["id"]?.let { id ->
                    log.debug("receive ping request with id -> $id")
                    call.respond(Pong("pong", Instant.now(), id))
                } ?: kotlin.run {
                    log.debug("receive ping request without id")
                    call.respond(HttpStatusCode.BadRequest, Error())
                }
            }
        }
    }.start(true)
}

data class Pong(val name: String, val timestamp: Instant, val id: String)

data class Error(val code: Int = -1, val description: String = "Unexpected Error")