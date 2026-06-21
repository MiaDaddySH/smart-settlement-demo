package com.yuangang.settlement.merchant;

import java.time.Instant;

public record MerchantResponse(
        Long id,
        String name,
        String externalReference,
        Instant createdAt
) {

    static MerchantResponse from(Merchant merchant) {
        return new MerchantResponse(merchant.getId(), merchant.getName(),
                merchant.getExternalReference(), merchant.getCreatedAt());
    }
}
