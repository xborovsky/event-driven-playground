package cz.marek_b.edp.retry_topics_dlq.listener;

import cz.marek_b.edp.retry_topics_dlq.event.ShippingLabelCreatedEvent;
import cz.marek_b.edp.retry_topics_dlq.service.ShippingLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShippingLabelCreatedListener {

    private final ShippingLabelService shippingLabelService;

    @KafkaListener(topics = "shipping.label-created", groupId = "shipping")
    public void onMessage(ShippingLabelCreatedEvent e) {
        shippingLabelService.handleShippingLabelCreated(e);
    }

}
