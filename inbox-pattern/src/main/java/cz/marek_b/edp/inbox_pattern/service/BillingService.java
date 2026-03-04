package cz.marek_b.edp.inbox_pattern.service;

import cz.marek_b.edp.inbox_pattern.data.BillingInbox;
import cz.marek_b.edp.inbox_pattern.event.ShippingLabelCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final BillingInboxService billingInboxService;

    @KafkaListener(topics = "shipping.label-created", containerFactory="kafkaListenerContainerFactory")
    public void handleLabelCreated(ConsumerRecord<String, ShippingLabelCreatedEvent> consumerRecord, Acknowledgment ack) {
        var billingInbox = new BillingInbox(
                consumerRecord.value().eventId(),
                Instant.now(),
                consumerRecord.topic(),
                consumerRecord.partition(),
                consumerRecord.offset()
        );

        try {
            billingInboxService.save(billingInbox, consumerRecord.value().shipmentId(), consumerRecord.value().labelId());
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage(), e);
        }

        ack.acknowledge();
    }

}
