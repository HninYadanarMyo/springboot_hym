package com.talent.java.batch11.springbootapp.transaction;

import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.repository.TransactionRepository;
import com.talent.java.batch11.springbootapp.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void testSaveTransaction_Success() {
        Transaction tx = new Transaction();
        tx.setAmount(5000.0);
        Mockito.when(transactionRepository.save(tx)).thenReturn(tx);
        Transaction savedTx = transactionService.saveTransaction(tx);
        Assertions.assertNotNull(savedTx);
        Assertions.assertEquals(5000.0, savedTx.getAmount());
        Mockito.verify(transactionRepository, Mockito.times(1)).save(tx);
    }
}
