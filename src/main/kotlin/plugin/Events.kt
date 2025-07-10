package com.example.plugin

import com.example.event.ConcurrentUserEvent
import com.example.event.HashSetChannel
import com.example.external.ReadRedisService
import com.example.model.currentSocketConnections
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject

val ConcurrentUserChannelKey =
    AttributeKey<HashSetChannel<ConcurrentUserEvent>>("ConcurrentUserChannel")

fun Application.configureEvent() {
    val channel = HashSetChannel<ConcurrentUserEvent>()
    attributes.put(ConcurrentUserChannelKey, channel)
}

fun Application.configureEventConsumer() {
    val channel = attributes[ConcurrentUserChannelKey]
    val readRedisService by inject<ReadRedisService>()
    val logger = KotlinLogging.logger {}

    launch {
        for (event in channel) {
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
}