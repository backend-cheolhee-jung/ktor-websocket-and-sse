package com.example.scheduler

import extension.ktor.schedule
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureConcurrentUserScheduler() {
    schedule(1.seconds) {
        TODO("여기서 동접자 정보 다 모아서 redis에 increaseBy or decreaseBy로 추가")
    }
}