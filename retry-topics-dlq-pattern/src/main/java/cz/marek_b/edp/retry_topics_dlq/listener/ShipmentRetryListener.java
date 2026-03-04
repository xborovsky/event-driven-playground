package cz.marek_b.edp.retry_topics_dlq.listener;

import cz.marek_b.edp.retry_topics_dlq.event.RetryMeta;
import cz.marek_b.edp.retry_topics_dlq.event.ShipmentCreatedEvent;
import cz.marek_b.edp.retry_topics_dlq.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ShipmentRetryListener {

    private final ObjectMapper objectMapper;
    private final ShippingService shippingService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @KafkaListener(topics = {"shipping.shipment-created.retry-1m", "shipping.shipment-created.retry-10m"}, groupId = "shipping")
    public void onMessage(
            ConsumerRecord<String, ShipmentCreatedEvent> record,
            Consumer<?, ?> consumer,
            Acknowledgment ack,
            @Header(value = ShippingService.RETRY_META_HEADER, required = false) byte[] retryMetaHeader,
            @Header(value = ShippingService.NOT_BEFORE_HEADER, required = false) byte[] notBeforeHeader
    ) {
        long nowMs = System.currentTimeMillis();
        Long notBeforeMs = parseNotBefore(notBeforeHeader);

        if (notBeforeMs != null && nowMs < notBeforeMs) {
            var tp = new TopicPartition(record.topic(), record.partition());

            consumer.pause(Set.of(tp));
            consumer.seek(tp, record.offset());

            long delayMs = Math.min(notBeforeMs - nowMs, 10 * 60 * 1000L);

            scheduler.schedule(() -> {
                try {
                    consumer.resume(Set.of(tp));
                } catch (Exception ignored) {
                    // v reálu zalogovat
                }
            }, delayMs, TimeUnit.MILLISECONDS);

            return;
        }

        var retryMeta = parseRetryMeta(retryMetaHeader);
        shippingService.handle(record.value(), retryMeta);
        ack.acknowledge();
    }

    private RetryMeta parseRetryMeta(byte[] header) {
        if (header == null) return null;
        return objectMapper.readValue(header, RetryMeta.class);
    }

    private Long parseNotBefore(byte[] header) {
        if (header == null) return null;

        return ByteBuffer.wrap(header).getLong();
    }

}
