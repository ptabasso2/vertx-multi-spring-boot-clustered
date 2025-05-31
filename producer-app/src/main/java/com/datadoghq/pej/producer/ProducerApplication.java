package com.datadoghq.pej.producer;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProducerApplication.class).run(args);
    }
    
    @Bean
    public Tracer tracer() {
        return GlobalOpenTelemetry.getTracer(ProducerApplication.class.getName(), "1.0.0");
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            VertxOptions options = new VertxOptions()
                    .setWorkerPoolSize(20)
                    .setEventLoopPoolSize(4)
                    .setHAEnabled(true)
                    .setQuorumSize(1); // optional: for HA

            Vertx.clusteredVertx(options)
                    .onSuccess(vertx -> {
                        System.out.println("Producer - Clustered Vert.x instance created");
                        ProducerVerticle producer = new ProducerVerticle();
                        vertx.deployVerticle(producer)
                                .onSuccess(id -> System.out.println("ProducerVerticle deployed: " + id));
                        vertx.deployVerticle(new GreetingVerticle())
                                .onSuccess(id -> System.out.println("GreetingVerticle deployed: " + id));
                        vertx.deployVerticle(new HttpServerVerticle())
                                .onSuccess(id -> System.out.println("HttpServerVerticle deployed: " + id));
                    })
                    .onFailure(err -> {
                        System.err.println("Failed to start clustered Vert.x: " + err.getMessage());
                        err.printStackTrace();
                    });
        };
    }
}