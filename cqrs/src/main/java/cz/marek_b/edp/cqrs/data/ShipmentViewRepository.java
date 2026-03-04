package cz.marek_b.edp.cqrs.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentViewRepository extends JpaRepository<ShipmentView, Long> {

    Optional<ShipmentView> findFirstByShipmentId(UUID shipmentId);

}
