package com.talent.java.batch11.springbootapp.repository;

import com.talent.java.batch11.springbootapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    public Account findByEmail(String email);
    public Account findByPhoneNumber(String phoneNumber);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByPhoneNumber(String phoneNumber);

}