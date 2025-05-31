package com.datadoghq.pej.consumer;

import io.vertx.core.AbstractVerticle;

public class ConsumerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        // CONSUMER - listens for messages and replies back
        vertx.eventBus().<String>consumer("consumer.message", message -> {
            System.out.println("Consumer received: " + message.body());
            // Process the message here

            // Reply back to the sender
            message.reply("Message processed successfully by consumer at " + System.currentTimeMillis());
        });

        System.out.println("ConsumerVerticle started and listening for messages");
    }
}
