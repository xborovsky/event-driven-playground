package cz.marek_b.edp.outbox_pattern.service;

import cz.marek_b.edp.outbox_pattern.data.ProcessedEvent;
import cz.marek_b.edp.outbox_pattern.data.ProcessedEventRepository;
import cz.marek_b.edp.outbox_pattern.data.Shipment;
import cz.marek_b.edp.outbox_pattern.data.ShipmentRepository;
import cz.marek_b.edp.outbox_pattern.event.OrderPaidEvent;
import cz.marek_b.edp.outbox_pattern.event.ShipmentCreatedInternalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ProcessedEventRepository processedEventRepo;
    private final ShipmentRepository shipmentRepo;
    private final ApplicationEventPublisher appEvents;

    @Transactional
    public void handle(OrderPaidEvent evt) {
        // 1) technická deduplikace
        if (processedEventRepo.existsById(evt.eventId())) {
            return; // duplicate -> no-op
        }

        processedEventRepo.save(new ProcessedEvent(evt.eventId(), Instant.now()));

        // 2) business idempotence přes unique(order_id)
        var shipmentId = UUID.randomUUID();
        try {
            shipmentRepo.save(new Shipment(
                    shipmentId,
                    evt.orderId(),
                    Instant.now(),
                    "CREATED"
            ));
        } catch (DataIntegrityViolationException e) {
            // už existuje shipment pro order -> ber jako validní no-op
            // (můžeš i načíst existující shipmentId a logovat)
            return;
        }

        // 3) publish "best effort" až po commitu transakce:
        appEvents.publishEvent(new ShipmentCreatedInternalEvent(shipmentId, evt.orderId()));
    }

}
