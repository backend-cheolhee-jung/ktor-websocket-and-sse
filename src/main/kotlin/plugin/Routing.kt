package com.example.plugin

import com.example.router.healthCheck
import com.example.router.socket
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        healthCheck()
        socket()
    }
}