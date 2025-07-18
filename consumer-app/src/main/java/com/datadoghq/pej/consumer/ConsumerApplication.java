package com.datadoghq.pej.consumer;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerApplication.class).run(args);
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
                        System.out.println("Consumer - Clustered Vert.x instance created");
                        vertx.deployVerticle(new ConsumerVerticle())
                                .onSuccess(id -> System.out.println("ConsumerVerticle deployed: " + id))
                                .onFailure(err -> {
                                    System.err.println("Failed to deploy ConsumerVerticle: " + err.getMessage());
                                    err.printStackTrace();
                                });
                    })
                    .onFailure(err -> {
                        System.err.println("Failed to start clustered Vert.x: " + err.getMessage());
                        err.printStackTrace();
                    });
        };
    }
}
