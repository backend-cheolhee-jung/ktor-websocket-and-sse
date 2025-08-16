package com.example.model

import io.ktor.server.sse.ServerSSESession
import kotlinx.coroutines.sync.Mutex

val sseConnectionsMutex = Mutex()
val currentSseConnections = HashSet<SSEConnection>()
val previousSseConnections = HashSet<SSEConnection>()

data class SSEConnection(
    val sessionId: String,
    val session: ServerSSESession,
)