package com.example.event

import com.example.external.ReadRedisService
import com.example.model.currentSocketConnections
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ConcurrentUserEventConsumer(
    private val readRedisService: ReadRedisService,
) : EventConsumer {
    private val logger = KotlinLogging.logger {}

    override suspend fun receiveEvent() = supervisorScope {
        val event = concurrentUserChannel.receive()
        val concurrentUserCount = readRedisService.get(event.key)?.toLong() ?: 0

        currentSocketConnections.forEach { connection ->
            launch {
                try {
                    connection.session.send(
                        Frame.Text("동접자 수: $concurrentUserCount")
                    )
                } catch (e: Exception) {
                    logger.error(e) { "session id: [${connection.sessionId}]에 메시지 전송 실패" }
                }
            }
        }
    }
}