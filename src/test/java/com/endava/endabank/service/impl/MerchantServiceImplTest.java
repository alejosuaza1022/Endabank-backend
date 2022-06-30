package com.endava.endabank.service.impl;

import com.endava.endabank.constants.MerchantStates;
import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.MerchantDao;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantGetFilterAuditDto;
import com.endava.endabank.dto.merchant.MerchantRegisterDto;
import com.endava.endabank.dto.merchant.MerchantRequestPaginationDto;
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
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MerchantServiceImplTest {

    @Mock
    private MerchantDao merchantDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MerchantRequestStateService merchantRequestStateService;

    @Mock
    private Pagination pagination;

    @Mock
    private Pageable pageable;

    @Mock
    private MerchantSpecification merchantSpecification;

    @Mock
    private UserService userService;

    @Mock
    private UserService secondUserService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp(){
        merchantService = new MerchantServiceImpl(merchantDao,userDao,modelMapper,merchantRequestStateService,userService,merchantSpecification,pagination,roleService);
    }

    @Test
    void testMerchantGetFilterAuditDtoShouldSuccessWhenDataCorrect() {
        MerchantFilterAuditDto merchantFilterAuditDto = new MerchantFilterAuditDto();
        Integer page = 0;
        when(modelMapper.map(merchantDao.findAll(
                        merchantSpecification.filterAuditMerchant(merchantFilterAuditDto), pageable),
                MerchantGetFilterAuditDto.class)).thenReturn(TestUtils.getMerchant());
        MerchantGetFilterAuditDto merchantGetFilterAuditDtoAnswer = merchantService.
                filterMerchantAudit(merchantFilterAuditDto, page);
        assertNotNull(merchantGetFilterAuditDtoAnswer);
    }

    @Test
    void testFindByIdShouldSuccessWhenDataCorrect(){
        Merchant merchantNotReviewed = TestUtils.getMerchantNotReviewed();

        when(merchantDao.findById(1)).thenReturn(Optional.of(merchantNotReviewed));

        Merchant merchant = merchantService.findById(1);

        assertEquals(merchantNotReviewed.getId(), merchant.getId());
        assertEquals(merchantNotReviewed.getTaxId(), merchant.getTaxId());
        assertEquals(merchantNotReviewed.getAddress(), merchant.getAddress());
        assertEquals(merchantNotReviewed.getStoreName(), merchant.getStoreName());
        assertEquals(merchantNotReviewed.getUser().getId(), merchant.getUser().getId());
        assertEquals(merchantNotReviewed.getMerchantRequestState().getId(), merchant.getMerchantRequestState().getId());

    }

    void testingGetAllMerchantRequests(){

    }

    @Test
    void testUpdateMerchantRequestStateToTrueShouldSuccessWheDataCorrect(){
        User adminUser = TestUtils.getUserAdmin2();
        User nonAdminUser = TestUtils.getUserNotAdmin();
        Merchant merchantNotReviewed = TestUtils.getMerchantNotReviewed();
        Merchant merchantApproved = TestUtils.getMerchantApproved();
        MerchantRequestState merchantRequestState = TestUtils.getApprovedMerchantRequestState();
        Role role = TestUtils.merchantRole();

        nonAdminUser.setRole(role);

        MerchantService merchantService1 = Mockito.spy(merchantService);
        UserService userService1 = Mockito.spy(userService);
        UserService userService2 = Mockito.spy(secondUserService);
        MerchantRequestStateService merchantRequestStateService1 = Mockito.spy(merchantRequestStateService);
        RoleService roleService1 = Mockito.spy(roleService);

        doReturn(merchantNotReviewed).when(merchantService1).findById(1);
        doReturn(adminUser).when(userService).findById(2);

        doReturn(merchantRequestState).when(merchantRequestStateService1).findById(2);
        doReturn(nonAdminUser).when(userService).findById(1);
        doReturn(role).when(roleService1).findById(3);

        when(userDao.save(TestUtils.getUserNotAdmin())).thenReturn(nonAdminUser);
        when(merchantDao.save(TestUtils.getMerchantApproved())).thenReturn(merchantApproved);

        Map<String,Object> map = merchantService1.updateMerchantRequestStatus(1,TestUtils.getUserPrincipalSecurity(),true);

        //assertEquals(HttpStatus.ACCEPTED.value(), map.get(Strings.STATUS_CODE_RESPONSE));
        assertEquals(Strings.MERCHANT_REQUEST_UPDATED, map.get(Strings.MESSAGE_RESPONSE));
    }

    @Test
    void testUpdateMerchantRequestStateToFalseShouldSuccessWheDataCorrect(){
        User adminUser = TestUtils.getUserAdmin2();
        User nonAdminUser = TestUtils.getUserNotAdmin();
        Merchant merchantNotReviewed = TestUtils.getMerchantApproved();
        Merchant merchantApproved = TestUtils.getMerchantApproved();
        MerchantRequestState merchantRequestState = TestUtils.getApprovedMerchantRequestState();
        Role role = TestUtils.merchantRole();

        nonAdminUser.setRole(role);

        MerchantService merchantService1 = Mockito.spy(merchantService);
        MerchantRequestStateService merchantRequestStateService1 = Mockito.spy(merchantRequestStateService);
        RoleService roleService1 = Mockito.spy(roleService);

        doReturn(merchantNotReviewed).when(merchantService1).findById(1);
        doReturn(adminUser).when(userService).findById(2);
        doReturn(nonAdminUser).when(userService).findById(1);
        doReturn(role).when(roleService1).findById(3);
        doReturn(merchantRequestState).when(merchantRequestStateService1).findById(2);

        when(userDao.save(TestUtils.getUserNotAdmin())).thenReturn(nonAdminUser);
        when(merchantDao.save(TestUtils.getMerchantApproved())).thenReturn(merchantApproved);

        Map<String,Object> map = merchantService1.updateMerchantRequestStatus(1,TestUtils.getUserPrincipalSecurity(),false);

        assertEquals(Strings.MERCHANT_REQUEST_UPDATED, map.get(Strings.MESSAGE_RESPONSE));
    }

    @Test
    void testGetAllMerchantRequests(){
        Sort sort = Sort.by("createAt").descending();
        Pageable pageable = pagination.getPageable(0,sort);

        when(merchantDao.findAll(pageable)).thenReturn(Page.empty());
        when(modelMapper.map(Page.empty(), MerchantRequestPaginationDto.class)).thenReturn(TestUtils.getMerchantRequestPaginationDto());

        MerchantRequestPaginationDto merchantRequestsPage = merchantService.getAllMerchantRequests(0);

        assertEquals(0,merchantRequestsPage.getTotalElements());
    }


    @Nested
    @DisplayName("Merchant save test")
    class SaveMerchantTests {
        MerchantRegisterDto merchantRegisterDto;

        @BeforeEach
        void setUp(){
            merchantRegisterDto = TestUtils.getMerchantRegisterDto();
            when(modelMapper.map(merchantRegisterDto, Merchant.class)).thenReturn(TestUtils.getMerchantNotReviewed());
        }

        @Test
        void testSaveShouldFailWhenUserAlreadyRegisterAMerchant(){
            User user = TestUtils.getUserNotAdmin();
            Integer userId = user.getId();

            when(userService.findById(user.getId())).thenReturn(user);
            when(merchantDao.findByUser(user))
                    .thenReturn(Optional.of(TestUtils.getMerchantNotReviewed()));
            assertThrows(UniqueConstraintViolationException.class, () -> merchantService.save(userId,merchantRegisterDto));
        }

        @Test
        void testSaveShouldFailWhenTaxIdAlreadyExists(){
            User user = TestUtils.getUserNotAdmin();
            Integer userId = user.getId();

            when(userService.findById(user.getId())).thenReturn(user);
            when(merchantDao.findByUser(user)).thenReturn(Optional.empty());
            when(merchantDao.findByTaxId(merchantRegisterDto.getTaxId()))
                    .thenReturn(Optional.of(TestUtils.getMerchantNotReviewed()));

            assertThrows(UniqueConstraintViolationException.class, () -> merchantService.save(userId,merchantRegisterDto));
        }

        @Test
        void testSaveShouldSuccessWhenDataCorrect(){
            User user = TestUtils.getUserNotAdmin();

            when(userService.findById(user.getId())).thenReturn(user);
            when(merchantDao.findByUser(user)).thenReturn(Optional.empty());
            when(merchantDao.findByTaxId(merchantRegisterDto.getTaxId())).thenReturn(Optional.empty());
            when(merchantRequestStateService.findById(MerchantStates.PENDING))
                    .thenReturn(TestUtils.getPendingMerchantRequestState());

            Map<String, String> map = merchantService.save(user.getId(), merchantRegisterDto);

            assertEquals(Strings.MERCHANT_REQUEST_CREATED,map.get(Strings.MESSAGE_RESPONSE));

        }
        @Test
        void testFindByMerchantKeyShouldFailWhenMerchantKeyNotCorrect(){
            when(merchantDao.findByMerchantKey("123")).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> merchantService.findByMerchantKey("123"));
        }
        @Test
        void testFindByMerchantKeyShouldSuccessWhenMerchantKeyCorrect(){
            Merchant merchant = TestUtils.getMerchantNotReviewed();
            when(merchantDao.findByMerchantKey("123")).thenReturn(Optional.of(merchant));
            assertEquals(merchant,merchantService.findByMerchantKey("123"));
        }
    }

}
