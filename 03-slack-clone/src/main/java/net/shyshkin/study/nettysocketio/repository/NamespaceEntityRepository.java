package net.shyshkin.study.nettysocketio.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.nettysocketio.model.NamespaceEntity;
import net.shyshkin.study.nettysocketio.model.RoomEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class NamespaceEntityRepository {

    private Map<String, NamespaceEntity> namespaces;

    @PostConstruct
    void init() {
        namespaces = new ConcurrentHashMap<>();
        NamespaceEntity wikiNs = NamespaceEntity.builder()
                .id(0)
                .nsTitle("Wiki")
                .img("https://upload.wikimedia.org/wikipedia/en/thumb/8/80/Wikipedia-logo-v2.svg/103px-Wikipedia-logo-v2.svg.png")
                .endpoint("/wiki")
                .build();
        NamespaceEntity mozillaNs = NamespaceEntity.builder()
                .id(1)
                .nsTitle("Mozilla")
                .img("https://www.mozilla.org/media/img/logos/firefox/logo-quantum.9c5e96634f92.png")
                .endpoint("/mozilla")
                .build();
        NamespaceEntity linuxNs = NamespaceEntity.builder()
                .id(2)
                .nsTitle("Linux")
                .img("https://upload.wikimedia.org/wikipedia/commons/a/af/Tux.png")
                .endpoint("/linux")
                .build();
        Stream.of(wikiNs, mozillaNs, linuxNs)
                .forEach(ns -> namespaces.put(ns.getEndpoint(), ns));

        wikiNs
                .addRoom(RoomEntity.builder().roomId(0).roomTitle("New Articles").namespace(wikiNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(1).roomTitle("Editors").namespace(wikiNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(2).roomTitle("Other").namespace(wikiNs.getNsTitle()).build());
        mozillaNs
                .addRoom(RoomEntity.builder().roomId(0).roomTitle("Firefox").namespace(mozillaNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(1).roomTitle("SeaMonkey").namespace(mozillaNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(2).roomTitle("SpiderMonkey").namespace(mozillaNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(3).roomTitle("Rust").namespace(mozillaNs.getNsTitle()).build());
        linuxNs
                .addRoom(RoomEntity.builder().roomId(0).roomTitle("Debian").namespace(linuxNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(1).roomTitle("Red Hat").namespace(linuxNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(2).roomTitle("MacOs").namespace(linuxNs.getNsTitle()).build())
                .addRoom(RoomEntity.builder().roomId(3).roomTitle("Kernal Development").namespace(linuxNs.getNsTitle()).build());
    }

    public Collection<NamespaceEntity> getAllNamespaces() {
        return namespaces.values();
    }

}
