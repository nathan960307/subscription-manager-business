package com.project.subscription.business.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class QueryMetricAspect {

    private final MeterRegistry registry;

    @Around("execution(* com.project..repository..*(..))")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("🔥 AOP 실행됨");

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();

            registry.timer("db.query.execution")
                    .record(end - start, TimeUnit.MILLISECONDS);
        }
    }
}
