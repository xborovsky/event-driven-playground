package cz.marek_b.edp.retry_topics_dlq.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_publisher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutboxPublisher {

    public enum Status { NEW, SENT, ERROR }

    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    private String topic;
    private String key;
    private String payloadJson;
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant sentAt;

    public OutboxPublisher(String topic, String key, String payloadJson) {
        this.topic = topic;
        this.key = key;
        this.payloadJson = payloadJson;
        this.createdAt = Instant.now();
        this.status = Status.NEW;
    }

}
