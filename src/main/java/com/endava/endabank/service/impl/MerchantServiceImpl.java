package com.endava.endabank.service.impl;

import com.endava.endabank.constants.MerchantStates;
import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.MerchantDao;
import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.exceptions.custom.UniqueConstraintViolationException;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.MerchantRequestState;
import com.endava.endabank.model.User;
import com.endava.endabank.service.MerchantRequestStateService;
import com.endava.endabank.service.MerchantService;
import com.endava.endabank.service.UserService;
import com.endava.endabank.specification.MerchantSpecification;
import com.endava.endabank.utils.Pagination;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private MerchantDao merchantDao;
    private ModelMapper modelMapper;
    private MerchantRequestStateService merchantRequestStateService;
    private UserService userService;
    private MerchantSpecification merchantSpecification;
    private Pagination pagination;

    @Override
    @Transactional(readOnly = true)
    public Merchant findById(Integer id) {
        return merchantDao.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(Strings.MERCHANT_NOT_FOUND));
    }

    @Override
    public Map<String,String> save(Integer userId, MerchantRegisterDto merchantDto) {
        Merchant merchant = modelMapper.map(merchantDto, Merchant.class);
        MerchantRequestState merchantRequestState = merchantRequestStateService.findById(MerchantStates.PENDING);

        Optional<Merchant> merchantExists = merchantDao.findByTaxId(merchant.getTaxId());
        if(merchantExists.isPresent()) throw new UniqueConstraintViolationException(Strings.CONSTRAINT_TAX_ID_VIOLATED);

        User user = userService.findById(userId);

        merchant.setUser(user);
        merchant.setMerchantRequestState(merchantRequestState);
        merchantDao.save(merchant);

        Map<String, String> map = new HashMap<>();
        map.put(Strings.MESSAGE_RESPONSE, Strings.MERCHANT_REQUEST_CREATED);

        return map;
    }

    @Override
    public MerchantGetFilterAuditDto filterMerchantAudit(MerchantFilterAuditDto merchantFilterAuditDto, Integer page) {
        Pageable pageable = pagination.getPageable(page);
        return modelMapper.map(merchantDao.findAll(
                merchantSpecification.filterAuditMerchant(merchantFilterAuditDto), pageable),
                MerchantGetFilterAuditDto.class);
    }
}
