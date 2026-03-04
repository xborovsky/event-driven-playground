package cz.marek_b.edp.cqrs.service;

import cz.marek_b.edp.cqrs.data.ShipmentView;
import cz.marek_b.edp.cqrs.data.ShipmentViewRepository;
import cz.marek_b.edp.cqrs.event.ShipmentCreatedEvent;
import cz.marek_b.edp.cqrs.event.ShippingLabelCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentViewRepository shipmentViewRepository;

    @Transactional
    public void upsertShipmentView(ShipmentCreatedEvent shipmentCreatedEvent) {
        var shipmentView = shipmentViewRepository.findFirstByShipmentId(shipmentCreatedEvent.shipmentId())
                .orElseGet(ShipmentView::new);

        if (shipmentView.getShipmentId() == null) {
            shipmentView.setShipmentId(shipmentCreatedEvent.shipmentId());
            shipmentView.setShipmentCreatedAt(shipmentCreatedEvent.createdAt());
            shipmentView.setStatus(ShipmentView.Status.CREATED);
        }
        shipmentView.setOrderId(shipmentCreatedEvent.orderId());

        saveShipmentView(shipmentView);
    }

    @Transactional
    public void upsertShipmentViewLabeled(ShippingLabelCreatedEvent shippingLabelCreatedEvent) {
        var shipmentView = shipmentViewRepository.findFirstByShipmentId(shippingLabelCreatedEvent.shipmentId())
                .orElseGet(ShipmentView::new);

        if (shipmentView.getShipmentId() == null) {
            shipmentView.setShipmentId(shippingLabelCreatedEvent.shipmentId());
            shipmentView.setShipmentCreatedAt(shippingLabelCreatedEvent.createdAt()); // placeholder, non-null field
            shipmentView.setOrderId(UUID.randomUUID()); // placeholder, non-null field
        }
        shipmentView.setStatus(ShipmentView.Status.LABELED);
        shipmentView.setLabelId(shippingLabelCreatedEvent.labelId());
        shipmentView.setLabelCreatedAt(shippingLabelCreatedEvent.createdAt());

        saveShipmentView(shipmentView);
    }

    private void saveShipmentView(ShipmentView shipmentView) {
        shipmentView.setUpdatedAt(Instant.now());

        shipmentViewRepository.save(shipmentView);
    }

}
