package cz.marek_b.edp.retry_topics_dlq.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipmentLabelRepository extends JpaRepository<ShipmentLabel, UUID> {

    boolean existsByShipmentId(UUID shipmentId);

}
