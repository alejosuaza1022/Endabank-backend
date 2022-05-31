package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.BankAccountDto;
import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Routes.API_ROUTE + Routes.ACCOUNT)
@AllArgsConstructor
public class AccountController {
    private BankAccountService bankAccountService;

    @GetMapping(Routes.DETAILS)
    public ResponseEntity<BankAccountDto> getAccountSummary(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountService.getAccountDetails(email));
    }

    @GetMapping(Routes.SUMMARY)
    public ResponseEntity<Page<TransactionDto>> getTransactionsSummary(@PathVariable String email, @PathVariable Integer page) {
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountService.getTransactionsSummary(email, page));
    }
}
