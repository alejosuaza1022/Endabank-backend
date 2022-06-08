package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.transaction.TransactionCreateDto;
import com.endava.endabank.dto.transaction.TransactionCreatedDto;
import com.endava.endabank.dto.transaction.TransactionFromMerchantDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(Routes.API_ROUTE + Routes.TRANSACTIONS)
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    @PostMapping(Routes.SEND_MONEY)
    public ResponseEntity<TransactionCreatedDto> createTransaction(Principal principal, @Valid @RequestBody TransactionCreateDto transactionCreateDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UserPrincipalSecurity user = (UserPrincipalSecurity) usernamePasswordAuthenticationToken.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(user.getId(), transactionCreateDto));
    }

    @PostMapping("/pay")
    public ResponseEntity<TransactionCreatedDto> createPayTransaction(@Valid @RequestBody TransactionFromMerchantDto transferFromMerchantDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransactionFromMerchant(transferFromMerchantDto));
    }
}
