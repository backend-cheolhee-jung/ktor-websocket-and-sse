package com.example.router

import com.example.model.SSEConnection
import com.example.model.currentSSEConnections
import com.example.model.sseConnectionsMutex
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.withLock
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun Route.sseRouter() {
    val logger = KotlinLogging.logger { }
    sse("/events") {
        heartbeat {
            period = 10.milliseconds
            event = ServerSentEvent("heartbeat")
        }

        val connection = SSEConnection(
            sessionId = UUID.randomUUID().toString(),
            session = this,
        )

        try {
            sseConnectionsMutex.withLock {
                currentSSEConnections.add(connection)
            }
            while (true) {
                delay(1.seconds)
            }
        } catch (e: Exception) {
            logger.info { "SSE connection failure: $e" }
        } finally {
            sseConnectionsMutex.withLock {
                currentSSEConnections.remove(connection)
            }
        }
    }
}