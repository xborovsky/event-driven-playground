package cz.marek_b.edp.retry_topics_dlq.service;

import cz.marek_b.edp.retry_topics_dlq.carrier_api.CarrierApi;
import cz.marek_b.edp.retry_topics_dlq.data.OutboxPublisher;
import cz.marek_b.edp.retry_topics_dlq.data.OutboxPublisherRepository;
import cz.marek_b.edp.retry_topics_dlq.data.ShipmentLabel;
import cz.marek_b.edp.retry_topics_dlq.data.ShipmentLabelRepository;
import cz.marek_b.edp.retry_topics_dlq.event.RetryMeta;
import cz.marek_b.edp.retry_topics_dlq.event.ShipmentCreatedEvent;
import cz.marek_b.edp.retry_topics_dlq.event.ShippingLabelCreatedEvent;
import cz.marek_b.edp.retry_topics_dlq.ex.CarrierTimeoutException;
import cz.marek_b.edp.retry_topics_dlq.ex.InvalidAddressException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingService {

    public static final String RETRY_META_HEADER = "retry-meta";
    public static final String NOT_BEFORE_HEADER = "not-before";

    private final CarrierApi carrierApi;
    private final KafkaTemplate<String, ShipmentCreatedEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OutboxPublisherRepository outboxPublisherRepository;
    private final ShipmentLabelRepository shipmentLabelRepository;

    @Transactional
    public void handle(ShipmentCreatedEvent evt, RetryMeta retryMeta) {
        try {
            carrierApi.doSomething();

            var shipmentLabel = shipmentLabelRepository.save(new ShipmentLabel(UUID.randomUUID()));

            var shippingLabelCreatedEvent = new ShippingLabelCreatedEvent(
                    UUID.randomUUID(),
                    shipmentLabel.getShipmentId(),
                    "TODO",
                    Instant.now()
            );

            outboxPublisherRepository.save(new OutboxPublisher("shipping.label-created", evt.orderId().toString(), objectMapper.writeValueAsString(shippingLabelCreatedEvent)));
        } catch (CarrierTimeoutException e) {
            handleTempRetry(evt, retryMeta, e.getMessage());
        } catch (InvalidAddressException e) {
            handleDLQ(evt);
        }
    }

    private void handleTempRetry(ShipmentCreatedEvent evt, RetryMeta retryMeta, String errorType) {
        ProducerRecord<String, ShipmentCreatedEvent> producerRecord = null;
        var now = Instant.now();

        if (retryMeta == null) {
            producerRecord = new ProducerRecord<>("shipping.shipment-created.retry-1m", evt.orderId().toString(), evt);
            producerRecord.headers().add(RETRY_META_HEADER, objectMapper.writeValueAsString(new RetryMeta(1, now, errorType)).getBytes());

            var epochMs = now.plus(1, ChronoUnit.MINUTES).toEpochMilli();
            producerRecord.headers().add(NOT_BEFORE_HEADER, ByteBuffer.allocate(8).putLong(epochMs).array());
        } else if (retryMeta.attempt() < 2) {
            producerRecord = new ProducerRecord<>("shipping.shipment-created.retry-10m", evt.orderId().toString(), evt);
            producerRecord.headers().add(RETRY_META_HEADER, objectMapper.writeValueAsString(new RetryMeta(retryMeta.attempt() + 1, now, errorType)).getBytes());

            var epochMs = now.plus(10, ChronoUnit.MINUTES).toEpochMilli();
            producerRecord.headers().add(NOT_BEFORE_HEADER, ByteBuffer.allocate(8).putLong(epochMs).array());
        }

        if (producerRecord == null) {
            handleDLQ(evt);
        } else {
            kafkaTemplate.send(producerRecord);
        }
    }

    private void handleDLQ(ShipmentCreatedEvent evt) {
        kafkaTemplate.send("shipping.shipment-created.dlq", evt.orderId().toString(), evt);
    }

}
