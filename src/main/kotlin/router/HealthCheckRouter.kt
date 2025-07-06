package com.example.router

import io.ktor.http.ContentType.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.healthCheck() {
    get("/") {
        call.respondText("healthy", contentType = Text.Plain)
    }
}