package com.example.property

import io.ktor.server.config.ApplicationConfig

data class RedisDataSource(
    val host: String,
    val port: Int,
    val password: String,
) {
    companion object {
        @JvmStatic
        fun of(
            config: ApplicationConfig,
        ) =
            with(config) {
                RedisDataSource(
                    host = propertyOrNull("ktor.redis.url")?.getString() ?: "localhost",
                    port = propertyOrNull("ktor.redis.port")?.getString()?.toInt() ?: 6379,
                    password = property("ktor.database.password").getString(),
                )
            }
    }
}