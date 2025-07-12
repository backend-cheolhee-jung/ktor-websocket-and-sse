package com.example.model

import io.ktor.server.sse.ServerSSESession

val currentSSEConnections = HashSet<SSEConnection>()
val previousSSEConnections = HashSet<SSEConnection>()

data class SSEConnection(
    val sessionId: String,
    val session: ServerSSESession,
)