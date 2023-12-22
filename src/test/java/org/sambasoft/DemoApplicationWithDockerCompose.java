package org.sambasoft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.Test;
import org.sambasoft.model.Order;
import org.sambasoft.repository.OrderRepository;
import org.sambasoft.service.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(properties = {"spring.docker.compose.skip.in-tests=false"})
@Testcontainers
public class DemoApplicationWithDockerCompose {

  @Autowired
  private OrderProducer orderProducer;
  @Autowired
  private OrderRepository orderRepository;

  @Test
  void testApplication() throws InterruptedException {

    Thread.sleep(10000);

    Order newOrder = Order.builder().name("test-order").amount(1000).build();

    orderProducer.sendOrder(newOrder);

    await().untilAsserted(() -> {
      Order order = orderRepository.findAll().getFirst();
      assertThat(order.getId()).isNotNull();
      assertThat(newOrder.getName()).isEqualTo(order.getName());
      assertThat(newOrder.getAmount()).isEqualTo(order.getAmount());
    });
  }

}
