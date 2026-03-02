package cz.marek_b.edp.outbox_pattern.event;

import java.util.UUID;

public record ShipmentCreatedInternalEvent(UUID shipmentId, UUID orderId) {
}
