package cz.marek_b.edp.inbox_pattern.config;

import cz.marek_b.edp.inbox_pattern.event.ShippingLabelCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShippingLabelCreatedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, ShippingLabelCreatedEvent> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ShippingLabelCreatedEvent>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

}
