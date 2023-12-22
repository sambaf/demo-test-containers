package org.sambasoft.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.sambasoft.model.Order;
import org.sambasoft.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log
@Component
@RequiredArgsConstructor
public class OrderConsumer {

  private final OrderRepository orderRepository;

  @KafkaListener(topics = {"${topic.name}"}, groupId = "order-id")
  @Transactional
  public void consumeOrder(Order order) {
    var createdOrder = orderRepository.save(order);
    log.info("order processed with id :" + createdOrder.getId());
  }


}
