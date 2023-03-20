package net.shyshkin.study.nettysocketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.nettysocketio.model.Progress;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocketServiceImpl implements SocketService {

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
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

    //analog io.emit(eventName,mess);
    @Override
    public void sendProgress(Progress progress) {
        server.getBroadcastOperations().sendEvent("progress", progress);
    }

}
