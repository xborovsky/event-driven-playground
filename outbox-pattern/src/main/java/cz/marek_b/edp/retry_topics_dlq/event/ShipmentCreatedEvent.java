package cz.marek_b.edp.retry_topics_dlq.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCreatedEvent(UUID id, UUID shipmentId, UUID orderId, Instant createdAt) {
}
