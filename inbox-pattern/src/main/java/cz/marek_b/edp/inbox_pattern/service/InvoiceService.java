package cz.marek_b.edp.inbox_pattern.service;

import cz.marek_b.edp.inbox_pattern.data.Invoice;
import cz.marek_b.edp.inbox_pattern.data.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Transactional
    public void createInvoice(UUID shipmentId, String labelId) {
        var invoice = new Invoice(shipmentId, labelId);
        invoiceRepository.save(invoice);
    }

}
