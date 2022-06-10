package com.endava.endabank.service;

import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.model.Merchant;

import java.util.Map;

public interface MerchantService {
    Merchant findById(Integer id);
    Merchant findByMerchantKey(String merchantKey);
    Map<String,String> save(Integer userId, MerchantRegisterDto merchantDto);
    MerchantGetFilterAuditDto filterMerchantAudit(MerchantFilterAuditDto merchantFilterAuditDto, Integer page);
}
