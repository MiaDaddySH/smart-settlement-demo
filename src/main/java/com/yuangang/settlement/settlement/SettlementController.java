package com.yuangang.settlement.settlement;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SettlementResponse create(@Valid @RequestBody CreateSettlementRequest request) {
        return settlementService.create(request);
    }

    @PatchMapping("/{id}/status")
    public SettlementResponse updateStatus(@PathVariable Long id,
            @Valid @RequestBody UpdateSettlementStatusRequest request) {
        return settlementService.updateStatus(id, request.status());
    }

    @GetMapping("/{id}")
    public SettlementResponse get(@PathVariable Long id) {
        return settlementService.get(id);
    }

    @GetMapping
    public List<SettlementResponse> list() {
        return settlementService.list();
    }

    @GetMapping("/{id}/history")
    public List<SettlementStatusHistoryResponse> history(@PathVariable Long id) {
        return settlementService.history(id);
    }
}
