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

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final SocketService socketService;
    private final ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {

        long totalTasks = 15;

        Long taskCount = Flux.interval(Duration.ofSeconds(1))
                .take(totalTasks)
                .map(i -> Progress.builder()
                        .currentTask(i + 1)
                        .totalTasks(totalTasks)
                        .build())
                .doOnNext(socketService::sendProgress)
                .doOnNext(progress -> log.info("Currently done {} of {}", progress.getCurrentTask(), progress.getTotalTasks()))
                .count()
                .block();

        log.info("Totally done {} tasks", taskCount);

        //Exit on finish work
        SpringApplication.exit(context);
    }


}
