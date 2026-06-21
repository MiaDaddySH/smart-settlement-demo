package com.yuangang.settlement.settlement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementStatusHistoryRepository extends JpaRepository<SettlementStatusHistory, Long> {

    List<SettlementStatusHistory> findAllBySettlementCaseIdOrderByChangedAtAsc(Long settlementCaseId);
}
