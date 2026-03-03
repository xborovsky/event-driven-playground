package cz.marek_b.edp.retry_topics_dlq.task;

import cz.marek_b.edp.retry_topics_dlq.data.OutboxPublisher;
import cz.marek_b.edp.retry_topics_dlq.data.OutboxPublisherRepository;
import cz.marek_b.edp.retry_topics_dlq.event.ShippingLabelCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OutboxPublishTask {

    private final OutboxPublisherRepository outboxPublisherRepository;
    private final KafkaTemplate<String, ShippingLabelCreatedEvent> kafkaTemplate;
    private final TransactionTemplate transactionTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5_000)
    public void publishFromOutbox() {
        var toPublish = outboxPublisherRepository.findFirst30ByStatusOrderByCreatedAtAsc(OutboxPublisher.Status.NEW);

        toPublish.forEach(outboxPublisher -> {
            transactionTemplate.executeWithoutResult(_ -> {
                try {

                    kafkaTemplate.send(
                            outboxPublisher.getTopic(),
                            outboxPublisher.getKey(),
                            objectMapper.readValue(outboxPublisher.getPayloadJson(), ShippingLabelCreatedEvent.class)
                    ).get(1_000, TimeUnit.MILLISECONDS);
                    outboxPublisher.setStatus(OutboxPublisher.Status.SENT);
                    outboxPublisher.setSentAt(Instant.now());
                } catch (Exception e) {
                    // todo log...
                }

                outboxPublisherRepository.save(outboxPublisher);
            });
        });
    }

}
