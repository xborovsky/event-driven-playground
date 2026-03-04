package cz.marek_b.edp.inbox_pattern.service;

import cz.marek_b.edp.inbox_pattern.data.BillingInbox;
import cz.marek_b.edp.inbox_pattern.data.BillingInboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingInboxService {

    private final BillingInboxRepository billingInboxRepository;
    private final InvoiceService invoiceService;

    @Transactional
    public void save(BillingInbox billingInbox, UUID shipmentId, String labelId) {
        billingInboxRepository.save(billingInbox);
        invoiceService.createInvoice(shipmentId, labelId);
    }

}
