package cz.marek_b.edp.retry_topics_dlq.event;

import java.time.Instant;
import java.util.UUID;

public record ShippingLabelCreatedEvent(
        UUID eventId,
        UUID shipmentId,
        String labelId,
        Instant createdAt
) {
}
