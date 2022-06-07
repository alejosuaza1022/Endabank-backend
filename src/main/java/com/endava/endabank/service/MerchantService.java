package com.endava.endabank.service;

import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.model.Merchant;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface MerchantService {
    Merchant findById(Integer id);

    Map<String, String> save(Integer userId, MerchantRegisterDto merchantDto);

    MerchantGetFilterAuditDto filterMerchantAudit(MerchantFilterAuditDto merchantFilterAuditDto, Integer page);

}
