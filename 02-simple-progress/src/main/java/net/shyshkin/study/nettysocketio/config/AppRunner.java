package net.shyshkin.study.nettysocketio.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.nettysocketio.SocketService;
import net.shyshkin.study.nettysocketio.model.Progress;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final SocketService socketService;
    private final ApplicationContext context;
    private final Sinks.Many<Progress> progressSink;
    private final Flux<Progress> progressBroadcast;

    @Override
    public void run(String... args) throws Exception {

        long totalTasks = 15;

        progressBroadcast
                .subscribe(socketService::sendProgress);

        Long taskCount = Flux.interval(Duration.ofSeconds(1))
                .take(totalTasks)
                .map(i -> Progress.builder()
                        .currentTask(i + 1)
                        .totalTasks(totalTasks)
                        .build())
                .doOnNext(progressSink::tryEmitNext)
                .doOnNext(progress -> log.info("Currently done {} of {}", progress.getCurrentTask(), progress.getTotalTasks()))
                .count()
                .block();

        log.info("Totally done {} tasks", taskCount);

        //Exit on finish work
        SpringApplication.exit(context);
    }


}
