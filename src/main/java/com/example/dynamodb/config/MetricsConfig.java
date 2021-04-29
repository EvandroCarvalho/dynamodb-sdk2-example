package com.example.dynamodb.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> registry() {
        return registry -> registry.config().commonTags("application", applicationName);
    }


    @Bean
    public Timer saveTimer(MeterRegistry registry) {
        return Timer.builder("save_time")
                .description("time to save")
                .tag("timer", "timer_salve")
                .register(registry);
    }

    @Bean
    public Timer consultScanTimer(MeterRegistry registry) {
        return Timer.builder("consult_scan_timer")
                .description("time to consult scan")
                .tag("timer", "timer_consult")
                .register(registry);
    }

    @Bean
    public Timer consultByIndex(MeterRegistry registry) {
        return Timer.builder("consult_by_index")
                .description("time to consult using index")
                .tag("timer", "timer_consult")
                .register(registry);
    }

    @Bean
    public Timer consultWithoutIndex(MeterRegistry registry) {
        return Timer.builder("consult_without_index")
                .description("time to consult without index")
                .tag("timer", "timer_consult")
                .register(registry);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
