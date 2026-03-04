package cz.marek_b.edp.retry_topics_dlq.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxPublisherRepository extends JpaRepository<OutboxPublisher, UUID> {

    List<OutboxPublisher> findFirst30ByStatusOrderByCreatedAtAsc(OutboxPublisher.Status status);

}
