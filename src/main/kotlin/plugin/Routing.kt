package com.example.plugin

import com.example.router.healthCheckRouter
import com.example.router.socketRouter
import com.example.router.sseRouter
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sse.SSE

fun Application.configureRouting() {
    install(SSE)
    routing {
        healthCheckRouter()
        socketRouter()
        sseRouter()
    }
}