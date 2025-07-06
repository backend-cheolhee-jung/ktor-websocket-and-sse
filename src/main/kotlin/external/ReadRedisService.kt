package com.example.external

import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await

class ReadRedisService(
    private val redisAsyncCommands: RedisAsyncCommands<String, String>,
) {
    suspend fun get(
        key: String,
    ): String? = redisAsyncCommands.get(key).await()
}