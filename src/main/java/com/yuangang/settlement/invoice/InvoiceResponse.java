package com.yuangang.settlement.invoice;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record InvoiceResponse(
        Long id,
        Long merchantId,
        Long customerId,
        String invoiceNumber,
        BigDecimal amount,
        String currency,
        LocalDate issueDate,
        LocalDate dueDate,
        InvoiceStatus status,
        Instant createdAt
) {

    static InvoiceResponse from(Invoice invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getMerchant().getId(), invoice.getCustomer().getId(),
                invoice.getInvoiceNumber(), invoice.getAmount(), invoice.getCurrency(), invoice.getIssueDate(),
                invoice.getDueDate(), invoice.getStatus(), invoice.getCreatedAt());
    }
}
