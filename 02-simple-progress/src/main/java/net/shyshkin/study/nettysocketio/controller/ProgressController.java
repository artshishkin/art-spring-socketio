package net.shyshkin.study.nettysocketio.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.nettysocketio.model.Progress;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final Flux<Progress> productBroadcast;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Progress> getProductBroadcast() {
        return productBroadcast;
    }

}
