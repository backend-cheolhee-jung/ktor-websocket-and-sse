package com.example.property

import io.ktor.server.config.*

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
                    host = propertyOrNull("ktor.redis.url")?.getString() ?: DEFAULT_REDIS_HOST,
                    port = propertyOrNull("ktor.redis.port")?.getString()?.toInt() ?: DEFAULT_REDIS_PORT,
                    password = property("ktor.database.password").getString(),
                )
            }

        const val DEFAULT_REDIS_HOST = "localhost"
        const val DEFAULT_REDIS_PORT = 6379
    }
}