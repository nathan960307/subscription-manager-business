package com.project.subscription.business.monitoring;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class CustomP6SpyFormatter implements MessageFormattingStrategy {

    private static QueryMetricService metricService;

    public static void setMetricService(QueryMetricService service) {
        metricService = service;
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed,
                                String category, String prepared, String sql, String url) {

        if (sql != null && !sql.trim().isEmpty()) {
            if (metricService != null) {
                metricService.record(elapsed);
            }
        }

        return sql;
    }
}
