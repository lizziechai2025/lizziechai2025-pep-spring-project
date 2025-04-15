package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    //create message
    public Message createMessage(Message newMessage){
        Message message = messageRepository.save(newMessage);
        return message;
    }


    //retrieve all messages
    public List<Message> getAllMessages(){
        return (List<Message>) messageRepository.findAll();
    }

    //retrieve message by id
    public Message getMessageById(Integer messageId){
        Optional<Message> optionalMessage = messageRepository.findByMessageId(messageId);
        if (optionalMessage.isPresent()){
            return optionalMessage.get();
        } 
        return null;

    }

    //delete message by id - should return rows deleted
    public void deleteMessageById(Integer messageId){
        messageRepository.deleteById(messageId);
    }
    

    //update message by id
    public Message updateMessageByMessageId(Integer messageId, String messageText){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage != null){
            Message updatedMessage = optionalMessage.get();
            updatedMessage.setMessageText(messageText);
            Message savedMessage = messageRepository.save(updatedMessage);
            return savedMessage;
        }
        return null;
    }

    //retrieve message based on accountId
    public List<Message> getAllMessagesByPostedBy(Integer accountId){
        return (List<Message>) messageRepository.findAllMessagesByPostedBy(accountId);
    }



}
