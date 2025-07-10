package com.example

import com.example.model.currentSocketConnections
import com.example.plugin.*
import com.example.scheduler.configureConcurrentUserScheduler
import com.example.util.CONCURRENT_USER_KEY
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSockets()
    configureRedis()
    configureRouting()
    configureFrameworks()
    configureConcurrentUserScheduler()
    configureEvent()
    configureEventConsumer()

    monitor.subscribe(ApplicationStopPreparing) {
        redisAsyncCommands.decrby(CONCURRENT_USER_KEY, currentSocketConnections.size.toLong())
        redisConnection.close()
        redisClient.shutdown()
    }
}
