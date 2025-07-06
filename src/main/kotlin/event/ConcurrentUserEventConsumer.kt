package com.example.event

import com.example.event.event.EventConsumer
import com.example.external.ReadRedisService
import com.example.model.ConcurrentUserEvent

class ConcurrentUserEventConsumer(
    private val readRedisService: ReadRedisService,
): EventConsumer<ConcurrentUserEvent, Long> {
    override suspend fun receiveEvent(event: ConcurrentUserEvent): Long {
        return readRedisService.get(event.key)?.toLong() ?: 0
    }
}