package com.project.subscription.business.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class QueryMetricService {

    private final Timer timer;

    public QueryMetricService(MeterRegistry registry) {
        this.timer = Timer.builder("db.query.execution")
                .description("DB query execution time")
                .register(registry);
    }

    public void record(long millis) {
        timer.record(millis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
