package com.example.model

import io.ktor.server.websocket.*
import kotlinx.coroutines.sync.Mutex

val socketConnectionsMutex = Mutex()
val currentSocketConnections = HashSet<SocketConnection>()
val previousSocketConnections = HashSet<SocketConnection>()

data class SocketConnection(
    val sessionId: String,
    val session: DefaultWebSocketServerSession,
)