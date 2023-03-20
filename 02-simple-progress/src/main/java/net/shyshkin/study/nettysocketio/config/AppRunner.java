package net.shyshkin.study.nettysocketio.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.nettysocketio.SocketService;
import net.shyshkin.study.nettysocketio.model.Progress;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final SocketService socketService;

    @Override
    public void run(String... args) throws Exception {

        long totalTasks = 300;

        Long taskCount = Flux.interval(Duration.ofSeconds(1))
                .take(totalTasks)
                .map(i -> Progress.builder()
                        .currentTask(i)
                        .totalTasks(totalTasks)
                        .build())
                .doOnNext(socketService::sendProgress)
                .doOnNext(progress -> log.info("Currently done {} of {}", progress.getCurrentTask(), progress.getTotalTasks()))
                .count()
                .block();

        log.info("Totally done {} tasks", taskCount);
    }


}
