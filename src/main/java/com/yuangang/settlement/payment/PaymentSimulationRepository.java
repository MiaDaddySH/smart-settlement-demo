package com.yuangang.settlement.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSimulationRepository extends JpaRepository<PaymentSimulation, Long> {

    List<PaymentSimulation> findAllBySettlementCaseIdOrderByCreatedAtDesc(Long settlementCaseId);
}
