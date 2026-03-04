package cz.marek_b.edp.cqrs.listeners;

import cz.marek_b.edp.cqrs.event.ShipmentCreatedEvent;
import cz.marek_b.edp.cqrs.event.ShippingLabelCreatedEvent;
import cz.marek_b.edp.cqrs.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShippingLabelCreatedListener {

    private final ShipmentService shipmentService;

    @KafkaListener(topics = "shipping.label-created")
    public void onShipmentCreated(ShippingLabelCreatedEvent shippingLabelCreatedEvent) {
        shipmentService.upsertShipmentViewLabeled(shippingLabelCreatedEvent);
    }

}
