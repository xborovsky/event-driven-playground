package cz.marek_b.edp.retry_topics_dlq.listener;

import cz.marek_b.edp.retry_topics_dlq.event.ShipmentCreatedEvent;
import cz.marek_b.edp.retry_topics_dlq.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentCreatedListener {

    private final ShippingService shippingService;

    @KafkaListener(topics = "shipping.shipment-created", groupId = "shipping")
    public void onMessage(ShipmentCreatedEvent e) {
        shippingService.handle(e, null);
    }

}
