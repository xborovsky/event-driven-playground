package cz.marek_b.edp.outbox_pattern.publisher;

import cz.marek_b.edp.outbox_pattern.event.ShipmentCreatedEvent;
import cz.marek_b.edp.outbox_pattern.event.ShipmentCreatedInternalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShipmentCreatedPublisher {

    private final KafkaTemplate<String, ShipmentCreatedEvent> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ShipmentCreatedInternalEvent e) {
        var evt = new ShipmentCreatedEvent(
                UUID.randomUUID(),
                e.shipmentId(),
                e.orderId(),
                Instant.now()
        );

        kafkaTemplate.send("shipping.shipment-created", e.orderId().toString(), evt);
    }

}
