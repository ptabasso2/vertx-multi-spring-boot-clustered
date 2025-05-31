package com.datadoghq.pej.producer;

import io.vertx.core.AbstractVerticle;

public class ProducerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        // CONSUMER - listens for incoming messages
        vertx.eventBus().<String>consumer("producer.trigger", message -> {
            triggerMessage();
            // Reply back to confirm the action
            message.reply("Message triggered successfully");
        });

        System.out.println("ProducerVerticle started and listening for triggers");
    }

    public void triggerMessage() {
        // Your existing message triggering logic
        System.out.println("Triggering message from producer...");
        // Send message to consumer or whatever your logic is
        vertx.eventBus().<String>request("consumer.message", "Hello from Producer!")
                .onSuccess(reply -> {
                    // This is where you read and print the consumer's reply!
                    System.out.println("Producer received reply from consumer: " + reply.body());
                })
                .onFailure(err -> {
                    System.err.println("Producer failed to get reply from consumer: " + err.getMessage());
                });
    }
}
