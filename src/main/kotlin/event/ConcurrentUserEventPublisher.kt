package com.example.event

val concurrentUserChannel = HashSetChannel<ConcurrentUserEvent>()

class ConcurrentUserEventPublisher: EventPublisher<ConcurrentUserEvent> {
    override suspend fun registerEvent(event: ConcurrentUserEvent) {
        concurrentUserChannel.send(event)
    }
}