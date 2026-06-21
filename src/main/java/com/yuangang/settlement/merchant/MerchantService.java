package com.yuangang.settlement.merchant;

import java.util.List;

import com.yuangang.settlement.audit.AuditService;
import com.yuangang.settlement.common.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final AuditService auditService;

    public MerchantService(MerchantRepository merchantRepository, AuditService auditService) {
        this.merchantRepository = merchantRepository;
        this.auditService = auditService;
    }

    @Transactional
    public MerchantResponse create(CreateMerchantRequest request) {
        merchantRepository.findByExternalReference(request.externalReference()).ifPresent(existing -> {
            throw new BusinessException("Merchant external reference already exists", HttpStatus.CONFLICT);
        });

        Merchant merchant = merchantRepository.save(new Merchant(request.name(), request.externalReference()));
        auditService.log("MERCHANT_CREATED", "Merchant", merchant.getId(), merchant.getExternalReference());
        return MerchantResponse.from(merchant);
    }

    @Transactional(readOnly = true)
    public List<MerchantResponse> list() {
        return merchantRepository.findAll().stream()
                .map(MerchantResponse::from)
                .toList();
    }
}
