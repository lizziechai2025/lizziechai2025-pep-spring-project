package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
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
        Account registeredAccount = accountService.register(newAccount);
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

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Integer postedBy = message.getPostedBy();
        String messageText = message.getMessageText();
        Integer messageLength = messageText.trim().length();

        //check message Text not blank and less than or equal to 255 chars
        if (messageLength == 0 || messageLength > 255){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //check if postedBy refers to a real, existitnig user
        Account account = accountService.findAccountByAccountId(postedBy);
        if (account == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //create message
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null){
            return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getAllMessages(@PathVariable Integer messageId){
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageById(messageId));
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<String> deleteMessageById(@PathVariable Integer messageId){
        Message messageToDelete = messageService.getMessageById(messageId);
        if (messageToDelete != null){
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(HttpStatus.OK).body("1");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
    }
    
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<String> updateMessageById(@PathVariable Integer messageId, @RequestBody Message updatedMessage){
        Message messageToUpdate =  messageService.getMessageById(messageId);
        if (messageToUpdate != null){
            String messageText = updatedMessage.getMessageText();
            Integer messageLength = messageText.trim().length();
            if (messageLength == 0 || messageLength > 255){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Message savedMessage = messageService.updateMessageByMessageId(messageId, messageText);
            if (savedMessage != null){
                return ResponseEntity.status(HttpStatus.OK).body("1");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable Integer accountId){
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessagesByPostedBy(accountId));
    }



}
