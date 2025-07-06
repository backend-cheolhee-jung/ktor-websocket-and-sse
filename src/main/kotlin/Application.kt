package com.example

import com.example.plugin.configureFrameworks
import com.example.plugin.configureRedis
import com.example.plugin.configureRouting
import com.example.plugin.configureSerialization
import com.example.plugin.configureSockets
import com.example.plugin.redisClient
import com.example.plugin.redisConnection
import com.example.scheduler.configureConcurrentUserScheduler
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

    monitor.subscribe(ApplicationStopPreparing) {
        redisConnection.close()
        redisClient.shutdown()
    }
}
