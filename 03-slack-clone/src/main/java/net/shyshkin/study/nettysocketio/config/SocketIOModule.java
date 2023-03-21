package net.shyshkin.study.nettysocketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.nettysocketio.repository.NamespaceEntityRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIOModule {

    private final SocketIOServer server;
    private final NamespaceEntityRepository repository;

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
            //send the ns data back to the client
            //socket.emit('nsList', namespaces);
            client.sendEvent("nsList", repository.getAllNamespaces());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.debug("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

}
