package org.sambasoft.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

  @Value("${topic.name}")
  private String topicName;

  @Bean
  NewTopic OrderTopic() {
    return TopicBuilder.name(topicName)
        .partitions(1)
        .replicas(1)
        .build();
  }

}
