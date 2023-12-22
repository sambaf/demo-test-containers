package org.sambasoft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.Test;
import org.sambasoft.model.Order;
import org.sambasoft.repository.OrderRepository;
import org.sambasoft.service.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class DemoApplicationTestsWithContainers {

  @Autowired
  private OrderProducer orderProducer;
  @Autowired
  private OrderRepository orderRepository;

  @Container
  static PostgreSQLContainer postgres =
      new PostgreSQLContainer("postgres:latest");


  @Container
  static KafkaContainer kafka = new KafkaContainer(
      DockerImageName.parse("confluentinc/cp-kafka:latest"));


  @DynamicPropertySource
  public static void setUp(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);

    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Test
  void testApplication() {

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
