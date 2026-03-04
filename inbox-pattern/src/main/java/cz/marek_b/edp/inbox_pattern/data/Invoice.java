package cz.marek_b.edp.inbox_pattern.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID shipmentId;
    private String labelId;
    private Instant createdAt;

    public Invoice(UUID shipmentId, String labelId) {
        this.shipmentId = shipmentId;
        this.labelId = labelId;
        this.createdAt = Instant.now();
    }

}
