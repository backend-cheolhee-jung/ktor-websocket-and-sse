package com.example.event

interface EventConsumer {
    suspend fun receiveEvent()
}