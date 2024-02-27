package com.dominicjmarshall.springbootday1.repositories;

import com.dominicjmarshall.springbootday1.models.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /* JPARepository provides CRUD functionality.
    With these generics it is tailored to work for my Account model. */    

    /* Spring Data JPA allows creation of query methods. */
    Optional<Account> findByEmailIgnoreCase(String email);

    /* In Account.java, passwordResetToken has its column name set to 'token' */
    Optional<Account> findByPasswordResetToken(String passwordResetToken);
}
