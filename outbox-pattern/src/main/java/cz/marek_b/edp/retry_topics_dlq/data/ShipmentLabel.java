package cz.marek_b.edp.retry_topics_dlq.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipment_label", uniqueConstraints = @UniqueConstraint(name = "uq_shipment_label_shipment", columnNames = "shipment_id"))
@Getter
@Setter
public class ShipmentLabel {

    @Id
    @Column(nullable = false)
    UUID id;

    @Column(name = "shipment_id", nullable = false)
    UUID shipmentId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}
