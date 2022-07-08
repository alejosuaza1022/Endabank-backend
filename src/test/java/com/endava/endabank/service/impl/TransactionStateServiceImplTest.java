package com.endava.endabank.service.impl;

import com.endava.endabank.dao.TransactionStateDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionStateServiceImplTest {
    @Mock
    private TransactionStateDao transactionStateDao;
    @InjectMocks
    private TransactionStateServiceImpl transactionStateService;

    @Test
    void testSaveTransactionStateShouldSuccess() {
        when(transactionStateDao.save(any())).thenReturn(null);
        transactionStateService.saveTransactionState(null, null, null);
        verify(transactionStateDao, times(1)).save(any());
    }
}
