package com.yuangang.settlement.payment;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/simulations")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentSimulationResponse simulate(@Valid @RequestBody SimulatePaymentRequest request) {
        return paymentService.simulate(request);
    }

    @GetMapping("/settlements/{settlementCaseId}/simulations")
    public List<PaymentSimulationResponse> listForSettlement(@PathVariable Long settlementCaseId) {
        return paymentService.listForSettlement(settlementCaseId);
    }
}
