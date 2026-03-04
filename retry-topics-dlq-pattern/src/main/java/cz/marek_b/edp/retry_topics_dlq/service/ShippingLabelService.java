package cz.marek_b.edp.retry_topics_dlq.service;

import cz.marek_b.edp.retry_topics_dlq.data.ShipmentLabelRepository;
import cz.marek_b.edp.retry_topics_dlq.event.ShippingLabelCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingLabelService {

    private final ShipmentLabelRepository shipmentLabelRepository;

    public void handleShippingLabelCreated(ShippingLabelCreatedEvent event) {
        var shipmentId = event.shipmentId();

        if (shipmentLabelRepository.existsByShipmentId(shipmentId)) {
            return;
        }

        System.out.println("do something...");
    }

}
