package com.example.scheduler

import com.example.event.ConcurrentUserEvent
import com.example.event.EventPublisher
import com.example.external.WriteRedisService
import com.example.model.currentSocketConnections
import com.example.model.previousSocketConnections
import com.example.model.socketConnectionsMutex
import com.example.model.currentSSEConnections
import com.example.model.previousSSEConnections
import com.example.model.sseConnectionsMutex
import com.example.util.CONCURRENT_USER_KEY
import extension.ktor.schedule
import io.ktor.server.application.*
import kotlinx.coroutines.sync.withLock
import org.koin.ktor.ext.inject
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds

fun Application.configureConcurrentUserScheduler() {
    val concurrentUserEventPublisher by inject<EventPublisher<ConcurrentUserEvent>>()
    val writeRedisService by inject<WriteRedisService>()

    schedule(1.seconds) {
        val event =
            socketConnectionsMutex.withLock {
                sseConnectionsMutex.withLock {
                    val currentUserSize = currentSocketConnections.size + currentSSEConnections.size
                    val previousUserSize = previousSocketConnections.size + previousSSEConnections.size
                    val addedUserSize = currentUserSize - previousUserSize

                    val addedUserAbsoluteSize = addedUserSize.absoluteValue.toLong()

                    if (addedUserSize > 0) writeRedisService.increaseBy(CONCURRENT_USER_KEY, addedUserAbsoluteSize)
                    else if (addedUserSize < 0) writeRedisService.decreaseBy(CONCURRENT_USER_KEY, addedUserAbsoluteSize)

                    previousSocketConnections.clear()
                    previousSocketConnections.addAll(currentSocketConnections)
                    previousSSEConnections.clear()
                    previousSSEConnections.addAll(currentSSEConnections)

                    ConcurrentUserEvent(CONCURRENT_USER_KEY)
                }
            }

        concurrentUserEventPublisher.registerEvent(event)
    }
}