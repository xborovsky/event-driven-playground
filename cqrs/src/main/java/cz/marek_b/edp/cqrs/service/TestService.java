package cz.marek_b.edp.cqrs.service;

import cz.marek_b.edp.cqrs.event.ShipmentCreatedEvent;
import cz.marek_b.edp.cqrs.event.ShippingLabelCreatedEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TestService {

    private final KafkaTemplate<String, ShipmentCreatedEvent> shipmentCreatedKT;
    private final KafkaTemplate<String, ShippingLabelCreatedEvent> shippingLabelCreatedKT;

    @PostConstruct
    public void initData() throws Exception {

        // vytvoříme pár shipmentIds
        var shipmentIds = Stream.generate(UUID::randomUUID)
                .limit(5)
                .toList();

        // 1️⃣ NORMAL FLOW
        for (UUID shipmentId : shipmentIds) {

            var shipmentEvent = new ShipmentCreatedEvent(
                    UUID.randomUUID(),
                    shipmentId,
                    UUID.randomUUID(),
                    Instant.now()
            );

            shipmentCreatedKT.send("shipping.shipment-created", shipmentId.toString(), shipmentEvent);

            Thread.sleep(100);

            var labelEvent = new ShippingLabelCreatedEvent(
                    UUID.randomUUID(),
                    shipmentId,
                    "LBL-" + shipmentId.toString().substring(0, 6),
                    Instant.now()
            );

            shippingLabelCreatedKT.send("shipping.label-created", shipmentId.toString(), labelEvent);
        }


        // 2️⃣ OUT OF ORDER (label přijde dřív)
        var outOfOrderShipment = UUID.randomUUID();

        shippingLabelCreatedKT.send(
                "shipping.label-created",
                outOfOrderShipment.toString(),
                new ShippingLabelCreatedEvent(
                        UUID.randomUUID(),
                        outOfOrderShipment,
                        "LBL-OOO",
                        Instant.now()
                )
        );

        Thread.sleep(100);

        shipmentCreatedKT.send(
                "shipping.shipment-created",
                outOfOrderShipment.toString(),
                new ShipmentCreatedEvent(
                        UUID.randomUUID(),
                        outOfOrderShipment,
                        UUID.randomUUID(),
                        Instant.now()
                )
        );


        // 3️⃣ DUPLICATE EVENT
        var dupShipment = UUID.randomUUID();

        var createdEvent = new ShipmentCreatedEvent(
                UUID.randomUUID(),
                dupShipment,
                UUID.randomUUID(),
                Instant.now()
        );

        shipmentCreatedKT.send("shipping.shipment-created", dupShipment.toString(), createdEvent);
        shipmentCreatedKT.send("shipping.shipment-created", dupShipment.toString(), createdEvent); // duplicate


        // 4️⃣ LABEL DUPLICATE
        var dupLabelShipment = UUID.randomUUID();

        var labelEvent = new ShippingLabelCreatedEvent(
                UUID.randomUUID(),
                dupLabelShipment,
                "LBL-DUP",
                Instant.now()
        );

        shippingLabelCreatedKT.send("shipping.label-created", dupLabelShipment.toString(), labelEvent);
        shippingLabelCreatedKT.send("shipping.label-created", dupLabelShipment.toString(), labelEvent);


        // 5️⃣ DLQ event (jen pro test observability)
        shipmentCreatedKT.send(
                "shipping.shipment-created.dlq",
                UUID.randomUUID().toString(),
                new ShipmentCreatedEvent(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        Instant.now()
                )
        );
    }

}

