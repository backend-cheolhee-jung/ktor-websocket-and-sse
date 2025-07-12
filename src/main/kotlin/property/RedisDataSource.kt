package com.example.property

import io.ktor.server.config.*

data class RedisDataSource(
    val host: String,
    val port: Int,
    val password: String?,
) {
    companion object {
        @JvmStatic
        fun of(
            config: ApplicationConfig,
        ) =
            with(config) {
                RedisDataSource(
                    host = propertyOrNull(DEFAULT_REDIS_URL_KEY)?.getString() ?: DEFAULT_REDIS_HOST,
                    port = propertyOrNull(DEFAULT_REDIS_PORT_KEY)?.getString()?.toInt() ?: DEFAULT_REDIS_PORT,
                    password = propertyOrNull(DEFAULT_REDIS_PASSWORD_KEY)?.getString() ,
                )
            }

        const val DEFAULT_REDIS_URL_KEY = "ktor.redis.url"
        const val DEFAULT_REDIS_PORT_KEY = "ktor.redis.port"
        const val DEFAULT_REDIS_PASSWORD_KEY = "ktor.redis.password"
        const val DEFAULT_REDIS_HOST = "localhost"
        const val DEFAULT_REDIS_PORT = 6379
    }
}