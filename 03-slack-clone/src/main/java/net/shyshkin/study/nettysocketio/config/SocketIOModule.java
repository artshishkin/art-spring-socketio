package net.shyshkin.study.nettysocketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.nettysocketio.model.MessageEntity;
import net.shyshkin.study.nettysocketio.model.MessageToServer;
import net.shyshkin.study.nettysocketio.model.RoomEntity;
import net.shyshkin.study.nettysocketio.repository.NamespaceEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

        repository.getAllNamespaces().forEach(ns -> {

            server.addNamespace(ns.getEndpoint())
                    .addConnectListener(nsSocket -> {
                        log.debug("Server received a connection to namespace {} from socket {}", ns.getEndpoint(), nsSocket.getSessionId());
                        String username = nsSocket.getHandshakeData()
                                .getSingleUrlParam("username");
                        nsSocket.sendEvent("nsRoomLoad", ns.getRooms());
                        nsSocket.getNamespace().addEventListener("joinRoom", String.class, (client, roomToJoin, ackSender) -> {

                            RoomEntity joinRoomEntity = ns.getRooms()
                                    .stream()
                                    .filter(room -> Objects.equals(room.getRoomTitle(), roomToJoin))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Room " + roomToJoin + " not found"));

                            client.joinRoom(roomToJoin);
                            int clientsCountInRoom = client.getNamespace()
                                    .getRoomOperations(roomToJoin)
                                    .getClients()
                                    .size();

                            String returnMsg = "You joined the room " + roomToJoin;
                            var history = joinRoomEntity.getHistory();

                            //analog JS callback
                            ackSender.sendAckData(clientsCountInRoom, returnMsg, history);

                            Set<String> allRooms = client.getAllRooms();
                            log.debug("client {} is joined to rooms: {}", client.getSessionId(), allRooms);

                            client.getNamespace().addEventListener("messageToServer", MessageToServer.class, (client1, msg, ackSender1) -> {
                                MessageEntity fullMsg = MessageEntity.builder()
                                        .text(msg.getText())
                                        .username(username)
                                        .time(new Date().getTime())
                                        .avatar(String.format("https://robohash.org/%s?set=set1&size=30x30", username))
                                        .build();
                                joinRoomEntity.addMessage(fullMsg);
                                client1.getNamespace().getRoomOperations(roomToJoin)
                                        .sendEvent("messageToClients", fullMsg);
                            });

                        });


                    });


        });

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
