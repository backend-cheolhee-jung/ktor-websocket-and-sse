package com.example.scheduler

import com.example.event.ConcurrentUserEventPublisher
import extension.ktor.schedule
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import kotlin.time.Duration.Companion.seconds

fun Application.configureConcurrentUserScheduler() {
    val concurrentUserEventPublisher by inject<ConcurrentUserEventPublisher>()

    schedule(1.seconds) {
        TODO("여기서 동접자 정보 다 모아서 redis에 increaseBy or decreaseBy로 추가")
    }
}