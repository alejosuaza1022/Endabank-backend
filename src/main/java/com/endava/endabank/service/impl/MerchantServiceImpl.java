package com.endava.endabank.service.impl;

import com.endava.endabank.constants.MerchantStates;
import com.endava.endabank.constants.Permissions;
import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.MerchantDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.dto.merchant.MerchantRequestPaginationDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.exceptions.custom.UniqueConstraintViolationException;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.MerchantRequestState;
import com.endava.endabank.model.Role;
import com.endava.endabank.model.User;
import com.endava.endabank.service.MerchantRequestStateService;
import com.endava.endabank.service.MerchantService;
import com.endava.endabank.service.RoleService;
import com.endava.endabank.service.UserService;
import com.endava.endabank.specification.MerchantSpecification;
import com.endava.endabank.utils.Pagination;
import com.endava.endabank.utils.merchant.MerchantUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private MerchantDao merchantDao;
    private UserDao userDao;
    private ModelMapper modelMapper;
    private MerchantRequestStateService merchantRequestStateService;
    private UserService userService;
    private MerchantSpecification merchantSpecification;
    private Pagination pagination;
    private RoleService roleService;

    @Override
    @Transactional(readOnly = true)
    public Merchant findById(Integer id) {
        return merchantDao.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(Strings.MERCHANT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Merchant findByMerchantKey(String merchantKey) {
        return merchantDao.findByMerchantKey(merchantKey).
                orElseThrow(() -> new ResourceNotFoundException(Strings.STATUS_ERROR + ": " +Strings.MERCHANT_NOT_FOUND));
    }

    @Override
    public Map<String,String> save(Integer userId, MerchantRegisterDto merchantDto) {
        Merchant merchant = modelMapper.map(merchantDto, Merchant.class);
        User user = userService.findById(userId);

        Optional<Merchant> merchantExists = merchantDao.findByUser(user);
        if(merchantExists.isPresent()){
            throw new UniqueConstraintViolationException(Strings.CONSTRAINT_MERCHANT_VIOLATED);
        }

        merchantExists = merchantDao.findByTaxId(merchant.getTaxId());
        if(merchantExists.isPresent()) {
            throw new UniqueConstraintViolationException(Strings.CONSTRAINT_TAX_ID_VIOLATED);
        }

        MerchantRequestState merchantRequestState = merchantRequestStateService.findById(MerchantStates.PENDING);

        merchant.setUser(user);
        merchant.setMerchantRequestState(merchantRequestState);
        merchantDao.save(merchant);

        Map<String, String> map = new HashMap<>();
        map.put(Strings.MESSAGE_RESPONSE, Strings.MERCHANT_REQUEST_UPDATED);

        return map;
    }

    @Override
    public MerchantGetFilterAuditDto filterMerchantAudit(MerchantFilterAuditDto merchantFilterAuditDto, Integer page) {
        Pageable pageable = pagination.getPageable(page);
        return modelMapper.map(merchantDao.findAll(
                merchantSpecification.filterAuditMerchant(merchantFilterAuditDto), pageable),
                MerchantGetFilterAuditDto.class);
    }

    @Override
    public MerchantRequestPaginationDto getAllMerchantRequests(Integer page) {
        Sort sort = Sort.by("createAt").descending();
        Pageable pageable = pagination.getPageable(page,sort);

        return modelMapper.map(merchantDao.findAll(pageable),MerchantRequestPaginationDto.class);
    }

    @Override
    public Map<String, String> updateMerchantRequestStatus(Integer id, UserPrincipalSecurity user, boolean value) {
        Merchant currentMerchant = this.findById(id);
        User reviewingUser = userService.findById(user.getId());
        MerchantRequestState newState;
        Map<String, String> map = new HashMap<>();

        if(value){
            newState = merchantRequestStateService.findById(MerchantStates.APPROVED);
            User requestingUser = userService.findById(currentMerchant.getUser().getId());
            Role newRole = roleService.findById(Permissions.ROLE_MERCHANT);
            String merchantKey = MerchantUtils.generateRandomKey(16);

            currentMerchant.setApiId(Strings.API_KEY);
            currentMerchant.setMerchantKey(merchantKey);

            requestingUser.setRole(newRole);
            userDao.save(requestingUser);

        } else{

            if(currentMerchant.getMerchantRequestState().getId().equals(MerchantStates.PENDING)){
                User requestingUser = userService.findById(currentMerchant.getUser().getId());
                Role newRole = roleService.findById(Permissions.ROLE_USER);

                currentMerchant.setApiId(null);
                currentMerchant.setMerchantKey(null);

                requestingUser.setRole(newRole);
                userDao.save(requestingUser);
            }

            newState = merchantRequestStateService.findById(MerchantStates.REJECTED);
        }

        currentMerchant.setReviewedBy(reviewingUser);
        currentMerchant.setMerchantRequestState(newState);
        map.put(Strings.MESSAGE_RESPONSE, Strings.MERCHANT_REQUEST_UPDATED);

        merchantDao.save(currentMerchant);

        return map;
    }
}
