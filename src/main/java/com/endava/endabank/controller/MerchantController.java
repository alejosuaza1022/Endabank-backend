package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.dto.merchant.MerchantRequestPaginationDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(Routes.GET_MERCHANT_REQUESTS)
    public ResponseEntity<MerchantRequestPaginationDto> getAllMerchantRequests(@PathVariable Integer page){
        return ResponseEntity.status(HttpStatus.OK).body(merchantService.getAllMerchantRequests(page));
    }

    @PutMapping(Routes.UPDATE_MERCHANT_REQUEST)
    public ResponseEntity<Map<String,String>> updateMerchantRequestState(@PathVariable Integer id, Principal principal, @RequestBody Map<String,Boolean> map){

        UsernamePasswordAuthenticationToken
                usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UserPrincipalSecurity user = (UserPrincipalSecurity) usernamePasswordAuthenticationToken.getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(merchantService.updateMerchantRequestStatus(id,user,map.get("value")));
    }

}
