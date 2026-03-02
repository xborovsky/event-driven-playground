package cz.marek_b.edp.outbox_pattern.event;

import java.util.UUID;

public record OrderPaidEvent(UUID eventId, UUID orderId) {
}
