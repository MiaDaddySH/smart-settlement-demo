package com.yuangang.settlement.settlement;

import java.util.List;

import com.yuangang.settlement.audit.AuditService;
import com.yuangang.settlement.common.BusinessException;
import com.yuangang.settlement.invoice.Invoice;
import com.yuangang.settlement.invoice.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettlementService {

    private final SettlementCaseRepository settlementCaseRepository;
    private final SettlementStatusHistoryRepository historyRepository;
    private final InvoiceRepository invoiceRepository;
    private final SettlementStatusTransitionValidator transitionValidator;
    private final AuditService auditService;

    public SettlementService(SettlementCaseRepository settlementCaseRepository,
            SettlementStatusHistoryRepository historyRepository, InvoiceRepository invoiceRepository,
            SettlementStatusTransitionValidator transitionValidator, AuditService auditService) {
        this.settlementCaseRepository = settlementCaseRepository;
        this.historyRepository = historyRepository;
        this.invoiceRepository = invoiceRepository;
        this.transitionValidator = transitionValidator;
        this.auditService = auditService;
    }

    @Transactional
    public SettlementResponse create(CreateSettlementRequest request) {
        Invoice invoice = invoiceRepository.findById(request.invoiceId())
                .orElseThrow(() -> new BusinessException("Invoice not found", HttpStatus.NOT_FOUND));
        if (settlementCaseRepository.existsByInvoiceId(invoice.getId())) {
            throw new BusinessException("Invoice already has a settlement case", HttpStatus.CONFLICT);
        }

        SettlementCase settlementCase = settlementCaseRepository.save(new SettlementCase(invoice));
        historyRepository.save(new SettlementStatusHistory(settlementCase, null, SettlementStatus.CREATED));
        auditService.log("SETTLEMENT_CREATED", "SettlementCase", settlementCase.getId(),
                "Invoice " + invoice.getId());
        return SettlementResponse.from(settlementCase);
    }

    @Transactional
    public SettlementResponse updateStatus(Long id, SettlementStatus targetStatus) {
        SettlementCase settlementCase = settlementCaseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Settlement case not found", HttpStatus.NOT_FOUND));
        SettlementStatus currentStatus = settlementCase.getStatus();
        transitionValidator.validate(currentStatus, targetStatus);

        settlementCase.changeStatus(targetStatus);
        historyRepository.save(new SettlementStatusHistory(settlementCase, currentStatus, targetStatus));
        auditService.log("SETTLEMENT_STATUS_CHANGED", "SettlementCase", settlementCase.getId(),
                currentStatus + " -> " + targetStatus);
        return SettlementResponse.from(settlementCase);
    }

    @Transactional(readOnly = true)
    public SettlementResponse get(Long id) {
        return settlementCaseRepository.findById(id)
                .map(SettlementResponse::from)
                .orElseThrow(() -> new BusinessException("Settlement case not found", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<SettlementResponse> list() {
        return settlementCaseRepository.findAll().stream()
                .map(SettlementResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SettlementStatusHistoryResponse> history(Long id) {
        if (!settlementCaseRepository.existsById(id)) {
            throw new BusinessException("Settlement case not found", HttpStatus.NOT_FOUND);
        }
        return historyRepository.findAllBySettlementCaseIdOrderByChangedAtAsc(id).stream()
                .map(SettlementStatusHistoryResponse::from)
                .toList();
    }
}
