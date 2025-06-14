package com.beratyesbek.springcloudgcpnotificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final PubSubTemplate pubSubTemplate;

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void subscribe() {
        pubSubTemplate.subscribe("order-subscription", message -> {
            try {
                String data = new String(message.getPubsubMessage().getData().toByteArray());
                Order order = objectMapper.readValue(data, Order.class);
                logger.info("Received order: ID={}, Product={}, Price={}",
                        order.getId(), order.getProduct(), order.getPrice());
                sendNotification(order);

                message.ack();
            } catch (Exception e) {
                logger.error("Error processing message", e);
                message.nack();
            }
        });
    }

    private void sendNotification(Order order) {
        logger.info("Sending notification for order: {}", order.getId());
    }
}