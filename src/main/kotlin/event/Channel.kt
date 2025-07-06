package com.example.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HashSetChannel<T> {
    private val channel = Channel<T>(Channel.UNLIMITED)
    private val seenEvents = HashSet<T>()
    private val mutex = Mutex()

    suspend fun send(
        event: T,
    ) {
        mutex.withLock {
            if (seenEvents.add(event)) {
                channel.send(event)
            }
        }
    }

    suspend fun receive() =
        channel.receive().apply {
            seenEvents.remove(this)
        }
}