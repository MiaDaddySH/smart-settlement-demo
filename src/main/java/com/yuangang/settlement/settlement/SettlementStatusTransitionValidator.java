package com.yuangang.settlement.settlement;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.yuangang.settlement.common.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class SettlementStatusTransitionValidator {

    private static final Map<SettlementStatus, Set<SettlementStatus>> ALLOWED_TRANSITIONS =
            new EnumMap<>(SettlementStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(SettlementStatus.CREATED,
                EnumSet.of(SettlementStatus.APPROVED, SettlementStatus.REJECTED));
        ALLOWED_TRANSITIONS.put(SettlementStatus.APPROVED,
                EnumSet.of(SettlementStatus.PAYMENT_PENDING));
        ALLOWED_TRANSITIONS.put(SettlementStatus.PAYMENT_PENDING,
                EnumSet.of(SettlementStatus.PAID, SettlementStatus.FAILED));
        ALLOWED_TRANSITIONS.put(SettlementStatus.REJECTED, EnumSet.noneOf(SettlementStatus.class));
        ALLOWED_TRANSITIONS.put(SettlementStatus.PAID, EnumSet.noneOf(SettlementStatus.class));
        ALLOWED_TRANSITIONS.put(SettlementStatus.FAILED, EnumSet.noneOf(SettlementStatus.class));
    }

    public void validate(SettlementStatus from, SettlementStatus to) {
        if (!ALLOWED_TRANSITIONS.getOrDefault(from, Set.of()).contains(to)) {
            throw new BusinessException("Invalid settlement status transition from " + from + " to " + to);
        }
    }

    public boolean isAllowed(SettlementStatus from, SettlementStatus to) {
        return ALLOWED_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
    }
}
