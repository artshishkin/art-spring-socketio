package net.shyshkin.study.nettysocketio;

import com.corundumstudio.socketio.SocketIOClient;
import net.shyshkin.study.nettysocketio.model.Message;
import net.shyshkin.study.nettysocketio.model.MessageType;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SocketService {

    public void sendMessage(String room, String eventName, SocketIOClient senderClient, String message) {

        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(room).getClients();

        Message mess = Message.builder()
                .message(message)
                .type(MessageType.SERVER)
                .room(room)
                .build();

        clients
                .stream()
//                .filter(client -> !Objects.equals(client.getSessionId(), senderClient.getSessionId()))
                .filter(client -> client != senderClient)
                .forEach(client ->
                        client.sendEvent(eventName, mess)
                );
    }
}
