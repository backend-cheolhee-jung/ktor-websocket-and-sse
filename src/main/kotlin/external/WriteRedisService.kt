package com.example.external

import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await
import java.time.ZonedDateTime

class WriteRedisService(
    private val redisAsyncCommands: RedisAsyncCommands<String, String>,
) {
    suspend fun set(
        key: String,
        value: String,
    ): String = redisAsyncCommands.set(key, value).await()

    suspend fun increase(
        key: String,
    ): Long = redisAsyncCommands.incr(key).await()

    suspend fun increaseBy(
        key: String,
        value: Long,
    ): Long = redisAsyncCommands.incrby(key, value).await()

    suspend fun decrease(
        key: String,
    ): Long = redisAsyncCommands.decr(key).await()

    suspend fun decreaseBy(
        key: String,
        value: Long,
    ): Long = redisAsyncCommands.decrby(key, value).await()

    suspend fun delete(
        key: String,
    ): Long = redisAsyncCommands.del(key).await()

    suspend fun exists(
        key: String,
    ): Boolean = redisAsyncCommands.exists(key).await() > 0

    suspend fun expire(
        key: String,
        seconds: Long,
    ): Boolean = redisAsyncCommands.expire(key, seconds).await()

    suspend fun expireAt(
        key: String,
        timestamp: ZonedDateTime,
    ): Boolean = redisAsyncCommands.expireat(key, timestamp.toInstant().toEpochMilli() / 1000).await()
}