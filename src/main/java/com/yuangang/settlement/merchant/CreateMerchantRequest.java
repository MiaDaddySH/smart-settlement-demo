package com.yuangang.settlement.merchant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMerchantRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 100) String externalReference
) {
}
