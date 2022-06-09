package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(Routes.API_ROUTE + Routes.MERCHANTS_ROUTE)
@AllArgsConstructor
public class MerchantController {

    private MerchantService merchantService;

    @PostMapping(Routes.CREATE_MERCHANT_REQUEST)
    public Map<String, String> create(Principal principal, @Valid @RequestBody MerchantRegisterDto merchant) {
        UsernamePasswordAuthenticationToken
                usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UserPrincipalSecurity user = (UserPrincipalSecurity) usernamePasswordAuthenticationToken.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(merchantService.save(user.getId(), merchant)).getBody();
    }

    @PostMapping(Routes.MERCHANT_FILTER)
    public ResponseEntity<MerchantGetFilterAuditDto> filterAuditMerchant(
            @RequestBody MerchantFilterAuditDto merchantFilterAuditDto, @PathVariable Integer page) {
        return ResponseEntity.status(HttpStatus.OK).
                body(merchantService.filterMerchantAudit(merchantFilterAuditDto, page));
    }

}
