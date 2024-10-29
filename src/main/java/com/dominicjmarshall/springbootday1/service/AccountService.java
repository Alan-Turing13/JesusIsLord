package com.dominicjmarshall.springbootday1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dominicjmarshall.springbootday1.models.Account;
import com.dominicjmarshall.springbootday1.models.Authority;
import com.dominicjmarshall.springbootday1.repositories.AccountRepository;
import com.dominicjmarshall.springbootday1.util.constants.Roles;

@Service
public class AccountService implements UserDetailsService{
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == null) {
            account.setRole(Roles.USER.getRole());
        }
        if (account.getPhoto() == null) {
            account.setPhoto("/images/kermit.webp");
        }
        return accountRepository.save(account);
    }

    public Account update(Account account){
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // READ Account from db using email address
        Optional<Account> optionalAccount = accountRepository.findByEmailIgnoreCase(email);
        if(!optionalAccount.isPresent()){
            throw new UsernameNotFoundException("Account not found");
        }
        Account account = optionalAccount.get();

        
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));

        for(Authority _auth: account.getAuthorities()){
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
        }
        
        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
    }

    public Optional<Account> findOneByEmail(String email){
        return accountRepository.findByEmailIgnoreCase(email);
    }

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }

    public Optional<Account> findByToken(String token){
        return accountRepository.findByPasswordResetToken(token);
    }
}
