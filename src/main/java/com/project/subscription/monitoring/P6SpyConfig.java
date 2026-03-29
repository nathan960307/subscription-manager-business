package com.project.subscription.monitoring;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class P6SpyConfig {

    private final QueryMetricService queryMetricService;

    @PostConstruct
    public void init() {
        CustomP6SpyFormatter.setMetricService(queryMetricService);
    }
}
