package cz.marek_b.edp.retry_topics_dlq.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipment", uniqueConstraints = @UniqueConstraint(name = "uq_shipment_order", columnNames = "order_id"))
@Getter
@Setter
public class Shipment {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String status;

    protected Shipment() {}

    public Shipment(UUID id, UUID orderId, Instant createdAt, String status) {
        this.id = id;
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.status = status;
    }

}

