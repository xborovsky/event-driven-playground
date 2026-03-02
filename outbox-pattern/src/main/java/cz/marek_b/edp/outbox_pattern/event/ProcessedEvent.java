package cz.marek_b.edp.outbox_pattern.event;

import java.time.Instant;
import java.util.UUID;

public record ProcessedEvent(UUID eventId, Instant timestamp) {
}
