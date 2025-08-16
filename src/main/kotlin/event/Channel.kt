package com.example.event

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HashSetChannel<T>(
    private val channel: Channel<T> = Channel(Channel.UNLIMITED),
) : Channel<T> by channel {
    private val seenEvents = LinkedHashSet<T>()
    private val mutex = Mutex()

    override suspend fun send(element: T) {
        mutex.withLock {
            if (seenEvents.add(element)) {
                channel.send(element)
            }

            if (seenEvents.size > EVENT_LIMIT) {
                val oldest = seenEvents.iterator().next()
                seenEvents.remove(oldest)
            }
        }
    }

    override suspend fun receive() =
        mutex.withLock {
            channel.receive().apply(seenEvents::remove)
        }

    @OptIn(InternalCoroutinesApi::class)
    override fun trySend(element: T) =
        if (mutex.tryLock()) {
            try {
                if (seenEvents.add(element)) {
                    channel.trySend(element)
                } else {
                    ChannelResult.failure()
                }
            } finally {
                mutex.unlock()
            }
        } else {
            ChannelResult.failure()
        }

    @OptIn(InternalCoroutinesApi::class)
    override fun tryReceive() =
        if (mutex.tryLock()) {
            try {
                channel.tryReceive().also { element ->
                    element.getOrNull()?.let(seenEvents::remove)
                }
            } finally {
                mutex.unlock()
            }
        } else {
            ChannelResult.failure()
        }

    companion object {
        private const val EVENT_LIMIT = 100_000
    }
}