package cz.marek_b.edp.inbox_pattern.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingInboxRepository extends JpaRepository<BillingInbox, UUID> {

}
