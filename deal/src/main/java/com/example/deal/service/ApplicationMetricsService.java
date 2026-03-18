package com.example.deal.service;

import com.example.deal.enums.ApplicationStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationMetricsService {

    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<ApplicationStatus, Counter> statusCounters = new ConcurrentHashMap<>();

    @PostConstruct
    public void initCounters() {
        for (ApplicationStatus status : ApplicationStatus.values()) {
            Counter counter = Counter.builder("deal.applications.status.count")
                    .description("Number of applications in each status")
                    .tag("status", status.name())
                    .register(meterRegistry);
            statusCounters.put(status, counter);
        }
        log.info("Application status counters initialized");
    }

    public void incrementStatusCount(ApplicationStatus status) {
        Counter counter = statusCounters.get(status);
        if (counter != null) {
            counter.increment();
            log.debug("Incremented counter for status: {}", status);
        }
    }
}