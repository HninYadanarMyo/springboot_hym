package com.talent.java.batch11.springbootapp.account;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountReposTest {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
////    @Test
////    @Commit
//    void saveAccount() {
//        Account account = new Account();
//        account.setName("Saung Hnin");
//        account.setEmail("saunghinin@gmail.com");
//        account.setPassword("123456");
//        account.setPhoneNumber("0911111111");
//        account.setBalance(5000.0);
//        Account savedAccount = accountRepository.save(account);
//
//        assertNotNull(savedAccount.getId());
//
//        Account retrievedAccount = accountRepository.findById(savedAccount.getId()).orElse(null);
//        assertNotNull(retrievedAccount);
//
//        assertEquals("Saung Hnin", retrievedAccount.getName());
//    }
////    @Test
//    void testFindByEmail(){
////        Account account = new Account();
////        account.setName("John Doe");
////        account.setEmail("john@gmail.com");
////        account.setPassword("1234");
////        account.setPhoneNumber("0922222222");
////        account.setBalance(10000.0);
////        accountRepository.save(account);
//
//        Account foundAccount = accountRepository.findByEmail("saunghinin@gmail.com");
//        assertNotNull(foundAccount);
//        assertEquals("Saung Hnin",foundAccount.getName());
//    }
////    @Test
//    void testFindByPhoneNumber(){
////        Account account = new Account();
////        account.setName("Li Li");
////        account.setEmail("lili@gmail.com");
////        account.setPassword("123456");
////        account.setPhoneNumber("099999999");
////        account.setBalance(80000.0);
////        accountRepository.save(account);
//
//        Account foundAccount = accountRepository.findByPhoneNumber("0911111111");
//        assertNotNull(foundAccount);
//        assertEquals("Saung Hnin",foundAccount.getName());
//
//    }
//    @Test
//    @Commit
//    void testUpdateBalance(){
////        Account account = new Account();
////        account.setName("Daisy");
////        account.setEmail("daisy@gmail.com");
////        account.setPassword("246810");
////        account.setPhoneNumber("0988888888");
////        account.setBalance(10000.0);
////        Account saveAccount = accountRepository.save(account);
//
//        Account toUpdate = accountRepository.findByPhoneNumber("0911111111");
//        assertNotNull(toUpdate);
//
//        toUpdate.setBalance(7000.0);
//        accountRepository.save(toUpdate);
//
////        Account updatedAccount = accountRepository.findByPhoneNumber("0911111111");
////        assertNotNull(updatedAccount);
//        assertEquals(7000.0,toUpdate.getBalance());
//    }

}