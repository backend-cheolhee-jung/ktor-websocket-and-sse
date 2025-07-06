package com.example.router

import com.example.external.ReadRedisService
import com.example.external.WriteRedisService
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject

fun Route.socket() {
    val redisReadService by application.inject<ReadRedisService>()
    val writeReadService by application.inject<WriteRedisService>()

    webSocket("/ws") {
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val text = frame.readText()
                outgoing.send(Frame.Text("YOU SAID: $text"))
                if (text.equals("bye", ignoreCase = true)) {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                }
            }
        }
    }
}