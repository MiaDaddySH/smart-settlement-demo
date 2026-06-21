package com.yuangang.settlement.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateInvoiceRequest(
        @NotNull @Positive Long merchantId,
        @NotNull @Positive Long customerId,
        @NotBlank @Size(max = 100) String invoiceNumber,
        @NotNull @DecimalMin(value = "0.01") @Digits(integer = 17, fraction = 2) BigDecimal amount,
        @NotBlank @Pattern(regexp = "[A-Za-z]{3}", message = "must be a 3-letter ISO currency code") String currency,
        @NotNull LocalDate issueDate,
        @NotNull LocalDate dueDate
) {
}
