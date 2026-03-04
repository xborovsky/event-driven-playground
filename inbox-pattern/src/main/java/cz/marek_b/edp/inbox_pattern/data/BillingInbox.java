package cz.marek_b.edp.inbox_pattern.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "billing_inbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingInbox {

    @Id
    private UUID eventId;
    private Instant receivedAt;
    private String topic;
    private int partition;
    private long offset;


}
