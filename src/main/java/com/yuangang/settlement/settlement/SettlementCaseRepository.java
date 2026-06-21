package com.yuangang.settlement.settlement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementCaseRepository extends JpaRepository<SettlementCase, Long> {

    boolean existsByInvoiceId(Long invoiceId);

    Optional<SettlementCase> findByInvoiceId(Long invoiceId);
}
