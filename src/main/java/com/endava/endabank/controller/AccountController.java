package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.CreateBankAccountDto;
import com.endava.endabank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(Routes.API_ROUTE)
@AllArgsConstructor
public class AccountController {

    private BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody CreateBankAccountDto banckAccount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bankAccountService.save(banckAccount));
    }
}
