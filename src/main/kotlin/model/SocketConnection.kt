package com.example.model

import io.ktor.server.websocket.*

val currentSocketConnections = HashSet<SocketConnection>()
val previousSocketConnections = HashSet<SocketConnection>()

data class SocketConnection(
    val sessionId: String,
    val session: DefaultWebSocketServerSession,
)