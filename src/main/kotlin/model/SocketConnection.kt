package com.example.model

val currentSocketConnections = HashSet<SocketConnection>()
val previousSocketConnections = HashSet<SocketConnection>()

data class SocketConnection(
    val sessionId: String,
)

@JvmInline
value class SocketPoolSize(
    val size: Long,
) {
    init {
        require(size < 20_000) { "하나의 서버는 20,000개 이상의 소켓을 처리할 수 없습니다." }
    }
}

data class ConcurrentUserEvent(
    val key: String = EVENT_NAME,
    val size: SocketPoolSize,
) {
    companion object {
        const val EVENT_NAME = "concurrent_user_event"
    }
}