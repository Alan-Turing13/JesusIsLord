package com.dominicjmarshall.springbootday1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dominicjmarshall.springbootday1.models.Authority;
import com.dominicjmarshall.springbootday1.repositories.AuthorityRepository;

@Service
public class AuthorityService {
    
    @Autowired
    private AuthorityRepository authorityRepository;

    public Authority save(Authority authority){
        return authorityRepository.save(authority);
    }

    public Optional<Authority> findById(Long id){
        return authorityRepository.findById(id);
    }
}
