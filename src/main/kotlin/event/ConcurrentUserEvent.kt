package com.example.event

import java.util.concurrent.locks.ReentrantLock

class ConcurrentUserEvent(
    val key: String
) {
    val id: Long = ConcurrentUserEventAutoIncrementBuilder.next()

    override fun equals(other: Any?): Boolean =
        other is ConcurrentUserEvent && key == other.key && id == other.id

    override fun hashCode(): Int = 31 * key.hashCode() + id.hashCode()

    override fun toString(): String = "ConcurrentUserEvent(key=$key, id=$id)"
}

object ConcurrentUserEventAutoIncrementBuilder {
    private val lock = ReentrantLock()
    private var currentId: Long = 0

    fun next(): Long {
        lock.lock()
        try {
            return currentId++
        } finally {
            lock.unlock()
        }
    }
}