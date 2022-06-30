package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.TransactionDto;
import com.endava.endabank.dto.bankaccount.BankAccountDto;
import com.endava.endabank.dto.bankaccount.CreateBankAccountDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(Routes.API_ROUTE + Routes.ACCOUNT)
@AllArgsConstructor
public class AccountController {
    private BankAccountService bankAccountService;

    @GetMapping(Routes.DETAILS)
    public ResponseEntity<BankAccountDto> getAccountSummary(Principal principal) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UserPrincipalSecurity user = (UserPrincipalSecurity) usernamePasswordAuthenticationToken.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountService.getAccountDetails(user.getEmail()));
    }

    @GetMapping(Routes.SUMMARY)
    public ResponseEntity<Page<TransactionDto>> getTransactionsSummary(Principal principal, @PathVariable Integer page) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UserPrincipalSecurity user = (UserPrincipalSecurity) usernamePasswordAuthenticationToken.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(bankAccountService.getTransactionsSummary(user.getEmail(), page));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody CreateBankAccountDto banckAccount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bankAccountService.save(banckAccount));
    }
}
