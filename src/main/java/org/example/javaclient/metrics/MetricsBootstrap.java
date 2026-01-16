package org.example.javaclient.metrics;

import io.dazzleduck.sql.micrometer.MicrometerForwarder;
import io.dazzleduck.sql.micrometer.config.MicrometerForwarderConfig;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import java.time.Duration;
import java.util.List;

public final class MetricsBootstrap {

    private static MicrometerForwarder forwarder;
    private static CompositeMeterRegistry registry;

    public static CompositeMeterRegistry start() {

        MicrometerForwarderConfig config = MicrometerForwarderConfig.builder()
                        .applicationId("java-client")
                        .applicationName("java-client-demo")
                        .applicationHost("localhost")
                        .baseUrl("http://localhost:8081")
                        .username("admin")
                        .password("admin")
                        .targetPath("metric")
                        .httpClientTimeout(Duration.ofSeconds(10))
                        .stepInterval(Duration.ofSeconds(10))
                        .minBatchSize(1024 * 1024)
                        .maxBatchSize(8 * 1024 * 1024)
                        .maxSendInterval(Duration.ofSeconds(2))
                        .maxInMemorySize(10 * 1024 * 1024)
                        .maxOnDiskSize(1024 * 1024 * 1024L)
                        .retryCount(3)
                        .retryIntervalMillis(1000)
                        .transformations(List.of())
                        .partitionBy(List.of())
                        .enabled(true)
                        .build();

        forwarder = new MicrometerForwarder(config);
        forwarder.start();
        registry = forwarder.getRegistry();
        return registry;
    }

    public static void shutdown() {
        if (forwarder != null) {
            forwarder.close();
        }
    }
}
