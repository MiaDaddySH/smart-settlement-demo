package com.yuangang.settlement.invoice;

import java.util.List;

import com.yuangang.settlement.audit.AuditService;
import com.yuangang.settlement.common.BusinessException;
import com.yuangang.settlement.customer.Customer;
import com.yuangang.settlement.customer.CustomerRepository;
import com.yuangang.settlement.merchant.Merchant;
import com.yuangang.settlement.merchant.MerchantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final AuditService auditService;

    public InvoiceService(InvoiceRepository invoiceRepository, MerchantRepository merchantRepository,
            CustomerRepository customerRepository, AuditService auditService) {
        this.invoiceRepository = invoiceRepository;
        this.merchantRepository = merchantRepository;
        this.customerRepository = customerRepository;
        this.auditService = auditService;
    }

    @Transactional
    public InvoiceResponse create(CreateInvoiceRequest request) {
        invoiceRepository.findByInvoiceNumber(request.invoiceNumber()).ifPresent(existing -> {
            throw new BusinessException("Invoice number already exists", HttpStatus.CONFLICT);
        });
        Merchant merchant = merchantRepository.findById(request.merchantId())
                .orElseThrow(() -> new BusinessException("Merchant not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new BusinessException("Customer not found", HttpStatus.NOT_FOUND));

        Invoice invoice = invoiceRepository.save(new Invoice(merchant, customer, request.invoiceNumber(),
                request.amount(), request.currency().toUpperCase(), request.issueDate(), request.dueDate()));
        auditService.log("INVOICE_CREATED", "Invoice", invoice.getId(), invoice.getInvoiceNumber());
        return InvoiceResponse.from(invoice);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse get(Long id) {
        return invoiceRepository.findById(id)
                .map(InvoiceResponse::from)
                .orElseThrow(() -> new BusinessException("Invoice not found", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> list() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceResponse::from)
                .toList();
    }
}
