package net.shyshkin.study.nettysocketio.config;

import net.shyshkin.study.nettysocketio.model.Progress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<Progress> sink() {
        return Sinks.many().replay().limit(1);
    }

    @Bean
    public Flux<Progress> progressBroadcast() {
        return sink().asFlux();
    }

}