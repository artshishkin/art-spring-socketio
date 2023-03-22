package net.shyshkin.study.nettysocketio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NamespaceEntity {

    private Integer id;
    private String nsTitle;
    private String img;
    private String endpoint;
    private final Set<RoomEntity> rooms = new HashSet<>();

    public NamespaceEntity addRoom(RoomEntity room) {
        this.rooms.add(room);
        return this;
    }

    public Optional<RoomEntity> getRoom(String roomName) {
        return rooms.stream()
                .filter(room -> Objects.equals(room.getRoomTitle(), roomName))
                .findAny();
    }

}


