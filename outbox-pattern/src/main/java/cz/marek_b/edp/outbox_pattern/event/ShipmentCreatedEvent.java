package cz.marek_b.edp.outbox_pattern.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCreatedEvent(UUID id, UUID shipmentId, UUID orderId, Instant timestamp) {
}
