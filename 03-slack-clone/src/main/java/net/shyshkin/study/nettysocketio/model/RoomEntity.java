package net.shyshkin.study.nettysocketio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEntity {

    private Integer roomId;
    private String roomTitle;
    private String namespace;
    @Builder.Default
    private boolean privateRoom = false;

    private final List<MessageEntity> history = new ArrayList<>();

    public RoomEntity addMessage(MessageEntity message) {
        this.history.add(message);
        return this;
    }

    public void clearHistory() {
        this.history.clear();
    }

}
