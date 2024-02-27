package com.dominicjmarshall.springbootday1.repositories;
import org.springframework.stereotype.Repository;

import com.dominicjmarshall.springbootday1.models.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    /* JPARepository provides CRUD functionality.
    With these generics it is tailored to work for my Authority model. */
}
