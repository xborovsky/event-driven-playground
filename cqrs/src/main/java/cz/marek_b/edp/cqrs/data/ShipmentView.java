package cz.marek_b.edp.cqrs.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipment_view")
@Getter
@Setter
@NoArgsConstructor
public class ShipmentView {

    public enum Status { CREATED, LABELED }

    @Id
    private UUID shipmentId;
    private UUID orderId;
    private Instant shipmentCreatedAt;
    private String labelId;
    private Instant labelCreatedAt;
    private Status status;
    private Instant updatedAt;

    public ShipmentView(UUID shipmentId, UUID orderId, Instant shipmentCreatedAt) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.shipmentCreatedAt = shipmentCreatedAt;
    }

}
