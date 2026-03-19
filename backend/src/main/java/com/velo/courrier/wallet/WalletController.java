package com.velo.courrier.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletRepository walletRepository;

    @GetMapping("/me/balance")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS')")
    public ResponseEntity<Map<String, BigDecimal>> getMyBalance(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        
        BigDecimal balance = walletRepository.findByUser_Id(userId)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);
                
        return ResponseEntity.ok(Map.of("balance", balance));
    }
}
