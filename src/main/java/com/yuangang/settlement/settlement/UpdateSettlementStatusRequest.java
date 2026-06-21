package com.yuangang.settlement.settlement;

import jakarta.validation.constraints.NotNull;

public record UpdateSettlementStatusRequest(@NotNull SettlementStatus status) {
}
