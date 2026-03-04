package cz.marek_b.edp.cqrs.listeners;

import cz.marek_b.edp.cqrs.event.ShipmentCreatedEvent;
import cz.marek_b.edp.cqrs.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentCreatedListener {

    private final ShipmentService shipmentService;

    @KafkaListener(topics = "shipping.shipment-created")
    public void onShipmentCreated(ShipmentCreatedEvent shipmentCreatedEvent) {
        shipmentService.upsertShipmentView(shipmentCreatedEvent);
    }

    @KafkaListener(topics = "shipping.shipment-created.dlq")
    public void onShipmentCreatedDlq(ShipmentCreatedEvent shipmentCreatedEvent) {
        log.info("Shipment created dlq received: {}", shipmentCreatedEvent);
    }

}
