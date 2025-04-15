package com.example.controller;

import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService){
        this.accountService = accountService;
    }


    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account newAccount)  {
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();

        //check username && password requirements
        if (username.trim().length() == 0 || password.length() < 4){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //check if account with same username exists
        Account doesSameUsernameExist = accountService.findAccountByUsername(newAccount);
        if (doesSameUsernameExist != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        //passed all reqs, register account
        accountService.register(newAccount);
        Account registeredAccount = accountService.findAccountByUsername(newAccount);
        if (registeredAccount != null){
            return ResponseEntity.status(HttpStatus.OK).body(registeredAccount);
        } else { //not able to register account for some reason
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) throws AuthenticationException {
        Optional<Account> optionalAccount = accountService.login(account.getUsername(), account.getPassword());
        if (optionalAccount.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(optionalAccount.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
