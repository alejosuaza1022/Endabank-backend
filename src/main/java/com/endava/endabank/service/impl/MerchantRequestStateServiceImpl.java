package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.MerchantRequestStateDao;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.MerchantRequestState;
import com.endava.endabank.service.MerchantRequestStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MerchantRequestStateServiceImpl implements MerchantRequestStateService {

    private MerchantRequestStateDao merchantRequestStatusDao;

    @Override
    @Transactional(readOnly = true)
    public MerchantRequestState findById(Integer id){
        return merchantRequestStatusDao.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(Strings.MERCHANT_REQUEST_STATE_NOT_FOUND));
    }
}
