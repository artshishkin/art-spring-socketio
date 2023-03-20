package net.shyshkin.study.nettysocketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIOModule {

    private final SocketIOServer server;

    @PostConstruct
    void init() {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.start();
    }

    @PreDestroy
    void shutDown() {
        server.stop();
    }

    private ConnectListener onConnected() {
        return (client) -> {
            log.debug("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.debug("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

}
