package org.example.javaclient;

import org.example.javaclient.metrics.MetricsBootstrap;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        // Metrics pipeline
        CompositeMeterRegistry registry = MetricsBootstrap.start();
        Counter heartbeat = registry.counter("java.client.heartbeat");

        Runtime.getRuntime().addShutdownHook(new Thread(MetricsBootstrap::shutdown));

        // Logs automatically handled by dazzleduck-sql-logger
        for (int i = 0; i < 100; i++) {
            log.info("Java client log {}", i);
            heartbeat.increment();
            Thread.sleep(2000);
        }

        log.info("Java client finished");
    }
}
