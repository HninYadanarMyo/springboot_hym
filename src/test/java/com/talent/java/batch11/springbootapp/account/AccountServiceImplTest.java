package com.talent.java.batch11.springbootapp.account;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.repository.TransactionRepository;
import com.talent.java.batch11.springbootapp.serviceImpl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @InjectMocks
    AccountServiceImpl accountService;
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionRepository transactionRepository;

//    @Test
    public void testDeposit_Success() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setBalance(10000.0);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        accountService.deposit(1L, 5000.0);
        Assertions.assertEquals(15000.0, mockAccount.getBalance());
        Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);
    }

//    @Test
    public void testWithdraw_Success() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setBalance(10000.0);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        accountService.withDraw(1L, 4000.0);
        Assertions.assertEquals(6000.0, mockAccount.getBalance());
    }

//    @Test
    public void testWithdraw_InsufficientBalance() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setBalance(2000.0);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountService.withDraw(1L, 5000.0);
        });
        Assertions.assertEquals("Insufficient balance", exception.getMessage());
    }
//    @Test
    public void testTransfer_Success() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setBalance(10000.0);
        sender.setPassword("password123");
        Account receiver = new Account();
        receiver.setId(2L);
        receiver.setBalance(3000.0);
        receiver.setPhoneNumber("0999999999");
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        Mockito.when(accountRepository.findByPhoneNumber("0999999999")).thenReturn(receiver);
        accountService.transfer(1L, "0999999999", 3000.0, "password123");
        Assertions.assertEquals(7000.0, sender.getBalance());
        Assertions.assertEquals(6000.0, receiver.getBalance());
    }
//    @Test
    public void testTransfer_WrongPassword() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setPassword("correct_password");

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountService.transfer(1L, "0999999999", 1000.0, "wrong_pass");
        });

        Assertions.assertEquals("Incorrect Password", exception.getMessage());
    }



    //@Test
    void saveAccount() {
        Account account = new Account();
        account.setId(1);
        account.setName("Saung");
        account.setEmail("saung@gmail.com");
        accountRepository.save(account);
        //accountService.saveAccount(account);

        verify(accountRepository).save(account);

        // when(accountRepository.findAccountByEmail("saung@gmail.com")).thenReturn(account);
    }
}
