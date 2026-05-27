package com.talent.java.batch11.springbootapp.repository;

import com.talent.java.batch11.springbootapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByEmail(String email);
    Account findByPhoneNumber(String phoneNumber);

}