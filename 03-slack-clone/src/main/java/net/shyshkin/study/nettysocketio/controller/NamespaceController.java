package net.shyshkin.study.nettysocketio.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.nettysocketio.model.NamespaceEntity;
import net.shyshkin.study.nettysocketio.repository.NamespaceEntityRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/namespaces")
@RequiredArgsConstructor
public class NamespaceController {

    private final NamespaceEntityRepository namespaceEntityRepository;

    @GetMapping
    public Collection<NamespaceEntity> getAllNamespaces() {
        return namespaceEntityRepository.getAllNamespaces();
    }

}
