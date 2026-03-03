package cz.marek_b.edp.inbox_pattern.event;

import java.time.Instant;
import java.util.UUID;

public record ShippingLabelCreatedEvent(
        UUID eventId,
        UUID shipmentId,
        String labelId,
        Instant createdAt
) {}