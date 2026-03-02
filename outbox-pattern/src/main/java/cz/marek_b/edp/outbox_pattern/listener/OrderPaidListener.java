package cz.marek_b.edp.outbox_pattern.listener;

import cz.marek_b.edp.outbox_pattern.event.OrderPaidEvent;
import cz.marek_b.edp.outbox_pattern.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPaidListener {

    private final ShippingService shippingService;

    @KafkaListener(topics = "order.paid", groupId = "shipping")
    public void onMessage(OrderPaidEvent e) {
        shippingService.handle(e);
    }

}
