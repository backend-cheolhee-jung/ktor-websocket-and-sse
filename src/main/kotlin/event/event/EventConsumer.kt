package com.example.event.event

interface EventConsumer<T: Any, R: Any> {
    suspend fun receiveEvent(
        event: T,
    ): R
}