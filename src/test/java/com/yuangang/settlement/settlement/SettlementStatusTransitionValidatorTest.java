package com.yuangang.settlement.settlement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import com.yuangang.settlement.common.BusinessException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

class SettlementStatusTransitionValidatorTest {

    private static final Set<Transition> VALID_TRANSITIONS = Set.of(
            new Transition(SettlementStatus.CREATED, SettlementStatus.APPROVED),
            new Transition(SettlementStatus.CREATED, SettlementStatus.REJECTED),
            new Transition(SettlementStatus.APPROVED, SettlementStatus.PAYMENT_PENDING),
            new Transition(SettlementStatus.PAYMENT_PENDING, SettlementStatus.PAID),
            new Transition(SettlementStatus.PAYMENT_PENDING, SettlementStatus.FAILED)
    );

    private final SettlementStatusTransitionValidator validator = new SettlementStatusTransitionValidator();

    @ParameterizedTest
    @MethodSource("validTransitions")
    void allowsValidTransitions(SettlementStatus from, SettlementStatus to) {
        assertDoesNotThrow(() -> validator.validate(from, to));
        assertTrue(validator.isAllowed(from, to));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitions")
    void rejectsInvalidTransitions(SettlementStatus from, SettlementStatus to) {
        assertThrows(BusinessException.class, () -> validator.validate(from, to));
        assertFalse(validator.isAllowed(from, to));
    }

    private static Stream<Arguments> validTransitions() {
        return VALID_TRANSITIONS.stream()
                .map(transition -> Arguments.of(transition.from(), transition.to()));
    }

    private static Stream<Arguments> invalidTransitions() {
        return EnumSet.allOf(SettlementStatus.class).stream()
                .flatMap(from -> EnumSet.allOf(SettlementStatus.class).stream()
                        .map(to -> new Transition(from, to)))
                .filter(transition -> !VALID_TRANSITIONS.contains(transition))
                .map(transition -> Arguments.of(transition.from(), transition.to()));
    }

    private record Transition(SettlementStatus from, SettlementStatus to) {
    }
}
