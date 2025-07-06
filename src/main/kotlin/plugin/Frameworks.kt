package com.example.plugin

import com.example.event.ConcurrentUserEventPublisher
import com.example.event.EventPublisher
import com.example.event.HashSetChannel
import com.example.model.ConcurrentUserEvent
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(redisModule, eventModule)
    }
}

val eventModule = module {
    single<HashSetChannel<ConcurrentUserEvent>> { HashSetChannel() }
    single<EventPublisher<ConcurrentUserEvent>> { ConcurrentUserEventPublisher() }
}