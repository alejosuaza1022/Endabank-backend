package com.endava.endabank.service.impl;

import com.endava.endabank.dao.MerchantRequestStateDao;
import com.endava.endabank.exceptions.custom.ResourceNotFoundException;
import com.endava.endabank.model.MerchantRequestState;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MerchantRequestStateServiceImpTest {

 @Mock
 private MerchantRequestStateDao merchantRequestStatusDao;

 @InjectMocks
 private MerchantRequestStateServiceImpl merchantRequestStateService;

 @Test
 void testFindByIdShouldSuccessWhenDataCorrect(){
  MerchantRequestState merchantRequestState = TestUtils.getPendingMerchantRequestState();
  Integer stateId = merchantRequestState.getId();

  when(merchantRequestStatusDao.findById(stateId)).thenReturn(Optional.of(merchantRequestState));

  MerchantRequestState merchantRequestState1 = merchantRequestStateService.findById(stateId);

  assertEquals(merchantRequestState,merchantRequestState1);

 }

 @Test
 void testFindByIdShouldFailWhenMerchantRequestStateNotFound(){
  MerchantRequestState merchantRequestState = TestUtils.getPendingMerchantRequestState();
  Integer stateId = merchantRequestState.getId();

  when(merchantRequestStatusDao.findById(stateId)).thenReturn(Optional.empty());

  assertThrows(ResourceNotFoundException.class, () -> merchantRequestStateService.findById(stateId));
 }



}