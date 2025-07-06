package com.example.plugin

import com.example.external.ReadRedisService
import com.example.external.WriteRedisService
import com.example.property.RedisDataSource
import io.ktor.server.application.*
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.async.RedisAsyncCommands
import org.koin.dsl.module

lateinit var redisClient: RedisClient
lateinit var redisConnection: StatefulRedisConnection<String, String>
lateinit var redisAsyncCommands: RedisAsyncCommands<String, String>

fun Application.configureRedis() {
    val redisDataSource = RedisDataSource.of(environment.config)
    redisClient = RedisClient.create("redis://${redisDataSource.host}:${redisDataSource.port}")
    redisConnection = redisClient.connect()
    redisAsyncCommands = redisConnection.async()
}

val redisModule = module {
    single<RedisClient> { redisClient }
    single<StatefulRedisConnection<String, String>> { redisConnection }
    single<RedisAsyncCommands<String, String>> { redisAsyncCommands }
    single<WriteRedisService> { WriteRedisService(redisAsyncCommands) }
    single<ReadRedisService> { ReadRedisService(redisAsyncCommands) }
}