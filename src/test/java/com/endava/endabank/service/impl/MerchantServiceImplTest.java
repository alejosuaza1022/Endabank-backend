package com.endava.endabank.service.impl;

import com.endava.endabank.dao.MerchantDao;
import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.specification.MerchantSpecification;
import com.endava.endabank.utils.Pagination;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceImplTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Pagination pagination;
    @Mock
    private Pageable pageable;
    @Mock
    private MerchantDao merchantDao;
    @Mock
    private MerchantSpecification merchantSpecification;
    @InjectMocks
    private MerchantServiceImpl merchantServiceImpl;


    @Test
    void testMerchantGetFilterAuditDtoShouldSuccessWhenDataCorrect() {
        MerchantFilterAuditDto merchantFilterAuditDto = new MerchantFilterAuditDto();
        Integer page = 0;
        when(modelMapper.map(merchantDao.findAll(
                        merchantSpecification.filterAuditMerchant(merchantFilterAuditDto), pageable),
                MerchantGetFilterAuditDto.class)).thenReturn(TestUtils.getMerchant());
        MerchantGetFilterAuditDto merchantGetFilterAuditDtoAnswer = merchantServiceImpl.
                filterMerchantAudit(merchantFilterAuditDto, page);
        assertNotNull(merchantGetFilterAuditDtoAnswer);
    }
}
