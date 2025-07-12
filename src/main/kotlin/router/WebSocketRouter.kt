package com.example.router

import com.example.model.SocketConnection
import com.example.model.currentSocketConnections
import com.example.model.connectionsMutex
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.sync.withLock
import java.util.*

/**
 * workflow:
 * 1. 소켓이 연결 될 때 마다 전역 변수인 currentSocketConnections에 연결된 세션을 저장합니다.
 * 2. 1초마다 스케줄러가 돌면서 currentSocketConnections를 확인하고, redis에 bulk increment 합니다.
 * 3. 소켓이 연결이 끊어지면 currentSocketConnections에서 세션을 삭제하고, redis에 bulk decrement 합니다.
 * 4. 먀번 increment, decrement 하면 레디스가 오버헤드를 많이 받기 때문에
 *    이전 SocketPoolSize와 현재 SocketPoolSize를 비교해서 increment 할 지 decrement 할 지 결정합니다.
 * 5. 레디스에 increment, decrement가 끝나면 socket에 연결된 사용자들에게 현재 동접자가 몇명인지 발송해줍니다.
 * 6. 이 부분은 event publisher와 event consumer로 구현합니다.
 */
fun Route.socket() {
    webSocket("/ws") {
        val connection = SocketConnection(
            sessionId = UUID.randomUUID().toString(),
            session = this,
        )

        try {
            connectionsMutex.withLock {
                currentSocketConnections.add(connection)
            }

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "커넥션 종료"))
                    }
                }
            }
        } finally {
            connectionsMutex.withLock {
                currentSocketConnections.remove(connection)
            }
        }
    }
}