package com.example.event

interface EventPublisher<T: Any> {
    suspend fun registerEvent(
        event: T,
    )
}