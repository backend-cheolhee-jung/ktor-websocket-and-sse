package com.example.plugin

import com.example.router.healthCheck
import com.example.router.socket
import com.example.router.sseRouter
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sse.SSE

fun Application.configureRouting() {
    install(SSE)
    routing {
        healthCheck()
        socket()
        sseRouter()
    }
}