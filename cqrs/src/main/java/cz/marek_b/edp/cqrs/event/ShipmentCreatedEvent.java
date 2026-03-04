package cz.marek_b.edp.cqrs.event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCreatedEvent(
        UUID eventId,
        UUID shipmentId,
        UUID orderId,
        Instant createdAt
) {}
