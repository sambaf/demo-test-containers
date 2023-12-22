package org.sambasoft.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.sambasoft.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log
@Component
@RequiredArgsConstructor
public class OrderProducer {

  @Value("${topic.name}")
  private String topicName;

  private final KafkaTemplate<String, Order> kafkaTemplate;

  public void sendOrder(Order order) {

    var producerFuture = kafkaTemplate.send(topicName, UUID.randomUUID().toString(), order);

    producerFuture.whenComplete((result, exception) -> {
      if (exception != null) {
        producerFuture.completeExceptionally(exception);
      } else {
        producerFuture.complete(result);
      }
    });
  }

}
