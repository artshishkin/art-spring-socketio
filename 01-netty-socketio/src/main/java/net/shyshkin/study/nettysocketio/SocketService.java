package net.shyshkin.study.nettysocketio;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import net.shyshkin.study.nettysocketio.model.Message;
import net.shyshkin.study.nettysocketio.model.MessageType;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    //analog socket.broadcast.to(room).emit(eventName,mess);
    //analog socket.to(room).emit(eventName,mess);
    public void sendMessageToRoomExceptSender(String room, String eventName, SocketIOClient socketIOClient, String message) {

        BroadcastOperations roomOperations = socketIOClient.getNamespace().getRoomOperations(room);

        Message mess = Message.builder()
                .message(message)
                .type(MessageType.SERVER)
                .room(room)
                .build();

        roomOperations
                .sendEvent(eventName, socketIOClient, mess);
    }

    //analog io.to(room).emit(eventName,mess);
    public void sendMessageToRoom(String room, String eventName, SocketIOClient senderClient, String message) {

        BroadcastOperations roomOperations = senderClient.getNamespace().getRoomOperations(room);

        Message mess = Message.builder()
                .message(message)
                .type(MessageType.SERVER)
                .room(room)
                .build();

        roomOperations.sendEvent(eventName, mess);
    }

    //analog io.emit(eventName,mess);
    public void sendMessageToAll(String eventName, SocketIOClient senderClient, String message) {

        BroadcastOperations broadcastOperations = senderClient.getNamespace().getBroadcastOperations();

        Message mess = Message.builder()
                .message(message)
                .type(MessageType.SERVER)
                .build();

        broadcastOperations.sendEvent(eventName, mess);
    }

}
