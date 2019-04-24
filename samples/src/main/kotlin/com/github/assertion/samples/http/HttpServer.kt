package com.github.assertion.samples.http

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat
import java.time.Instant
import java.util.*

data class Pong(val name: String, val timestamp: Instant, val id: String)

data class Error(val code: Int = -1, val description: String = "Unexpected Error")

class HttpServer {

    fun start() {
        val cardService = CardService()
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
                        println("receive ping request with id -> $id")
                        call.respond(Pong("pong", Instant.now(), id))
                    } ?: run {
                        println("receive ping request without id")
                        call.respond(HttpStatusCode.BadRequest, Error())
                    }
                }
                put("/card/issue/") {
                    val cardRq = call.receive(CardIssueRq::class)
                    println("Receive issue request $cardRq")
                    val rs = cardService.issue(cardRq)
                    println("Successfully issue card $rs")
                    call.respond(rs)
                }
                post("/card/status") {
                    val statusRq = call.receive(CardStatusRq::class)
                    println("Receive change status request $statusRq")
                    val rs = cardService.status(statusRq)
                    println("Successfully change status $rs")
                    call.respond(rs)
                }
                get("/card/info/") {
                    val id = call.request.queryParameters["id"]
                    val pan = call.request.queryParameters["pan"]
                    val psn = Integer.valueOf(call.request.queryParameters["psn"])
                    println("Receive info request with id: $id, pan: $pan, psn: $psn")
                    Objects.requireNonNull(id, "id")
                    Objects.requireNonNull(id, "pan")
                    Objects.requireNonNull(id, "psn")
                    val card = cardService.info(CardInfoRq(id!!, pan!!, psn!!))
                    println("send response $card")
                    call.respond(card)
                }

            }
        }.start(false)
    }


}

fun main() {
    val server = HttpServer()
    server.start()
}