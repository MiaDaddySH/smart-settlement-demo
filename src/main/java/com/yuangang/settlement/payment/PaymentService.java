package com.yuangang.settlement.payment;

import java.util.List;
import java.util.UUID;

import com.yuangang.settlement.audit.AuditService;
import com.yuangang.settlement.common.BusinessException;
import com.yuangang.settlement.settlement.SettlementCase;
import com.yuangang.settlement.settlement.SettlementCaseRepository;
import com.yuangang.settlement.settlement.SettlementService;
import com.yuangang.settlement.settlement.SettlementStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentSimulationRepository paymentSimulationRepository;
    private final SettlementCaseRepository settlementCaseRepository;
    private final SettlementService settlementService;
    private final AuditService auditService;

    public PaymentService(PaymentSimulationRepository paymentSimulationRepository,
            SettlementCaseRepository settlementCaseRepository, SettlementService settlementService,
            AuditService auditService) {
        this.paymentSimulationRepository = paymentSimulationRepository;
        this.settlementCaseRepository = settlementCaseRepository;
        this.settlementService = settlementService;
        this.auditService = auditService;
    }

    @Transactional
    public PaymentSimulationResponse simulate(SimulatePaymentRequest request) {
        SettlementCase settlementCase = settlementCaseRepository.findById(request.settlementCaseId())
                .orElseThrow(() -> new BusinessException("Settlement case not found", HttpStatus.NOT_FOUND));

        PaymentStatus paymentStatus = request.successful() ? PaymentStatus.SUCCEEDED : PaymentStatus.FAILED;
        SettlementStatus settlementStatus = request.successful() ? SettlementStatus.PAID : SettlementStatus.FAILED;

        PaymentSimulation paymentSimulation = paymentSimulationRepository.save(new PaymentSimulation(
                settlementCase, paymentStatus, "SIM-" + UUID.randomUUID()));
        settlementService.updateStatus(settlementCase.getId(), settlementStatus);
        auditService.log("PAYMENT_SIMULATED", "PaymentSimulation", paymentSimulation.getId(),
                "Settlement " + settlementCase.getId() + " " + paymentStatus);
        return PaymentSimulationResponse.from(paymentSimulation);
    }

    @Transactional(readOnly = true)
    public List<PaymentSimulationResponse> listForSettlement(Long settlementCaseId) {
        if (!settlementCaseRepository.existsById(settlementCaseId)) {
            throw new BusinessException("Settlement case not found", HttpStatus.NOT_FOUND);
        }
        return paymentSimulationRepository.findAllBySettlementCaseIdOrderByCreatedAtDesc(settlementCaseId).stream()
                .map(PaymentSimulationResponse::from)
                .toList();
    }
}
