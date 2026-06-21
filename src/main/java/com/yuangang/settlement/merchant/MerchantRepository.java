package com.yuangang.settlement.merchant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByExternalReference(String externalReference);
}
