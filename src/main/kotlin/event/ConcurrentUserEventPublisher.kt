package com.example.event

import com.example.model.ConcurrentUserEvent

val concurrentUserChannel = HashSetChannel<ConcurrentUserEvent>()

class ConcurrentUserEventPublisher: EventPublisher<ConcurrentUserEvent> {
    override suspend fun registerEvent(event: ConcurrentUserEvent) {
        concurrentUserChannel.send(event)
    }
}