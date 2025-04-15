package com.example.service;

import java.util.Optional;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /**
     * @param newAccount
     */
    public Account register(Account newAccount){
        Account account = accountRepository.save(newAccount);
        return account;
    }

    /**
     * @param username string
     * @param password string
     */
    public Optional<Account> login(String username, String password ) throws AuthenticationException {
       Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
       return account;
    }

    /**
     * @param account
     */
    public Account findAccountByUsername(Account account){
        Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
        if (optionalAccount.isPresent()){
            return optionalAccount.get();
        } 
        
        return null;
    }

    /**
     * @param account
     */
    public Account findAccountByAccountId(Integer accountId){
        Optional<Account> optionalAccount = accountRepository.findByAccountId(accountId);
        if (optionalAccount.isPresent()){
            return optionalAccount.get();
        } 
        
        return null;
    }

    
}
