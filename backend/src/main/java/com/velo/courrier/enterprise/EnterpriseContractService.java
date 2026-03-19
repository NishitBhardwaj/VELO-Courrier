package com.velo.courrier.enterprise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnterpriseContractService {

    // private final EnterpriseContractRepository contractRepository;

    /**
     * Intercepts standard fare estimates natively, evaluating runtime boolean rules 
     * on active B2B Enterprise Agreements.
     */
    public BigDecimal applyContractDiscounts(UUID customerId, BigDecimal baseFare) {
        log.debug("Evaluating potential Enterprise Pricing Contract for Customer: {}", customerId);

        /*
        Optional<EnterpriseContract> activeContract = contractRepository.findByCustomerUserIdAndActiveTrue(customerId);
        
        if (activeContract.isPresent()) {
            BigDecimal discountPercent = activeContract.get().getDiscountPercentage();
            if (discountPercent != null && discountPercent.compareTo(BigDecimal.ZERO) > 0) {
                
                // baseFare * (1 - (discount / 100))
                BigDecimal multiplier = BigDecimal.ONE.subtract(discountPercent.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                BigDecimal finalFare = baseFare.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
                
                log.info("Applied {}% Enterprise Discount. Base: ${} -> Final: ${}", discountPercent, baseFare, finalFare);
                return finalFare;
            }
        }
        */

        // Scaffold Mock Fallback
        return baseFare;
    }

    public String resolvePriorityTier(UUID customerId) {
        /*
        return contractRepository.findByCustomerUserIdAndActiveTrue(customerId)
                .map(EnterpriseContract::getSlaPriorityTier)
                .orElse("STANDARD");
        */
        return "STANDARD";
    }
}
