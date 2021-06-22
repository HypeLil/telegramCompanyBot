package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.MessageEntity;
import com.nikita.telegramBot.repo.JpaMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private JpaMessageRepository repository;

    public List<MessageEntity> findAllByChatId(String chatId){
        return repository.findAllByChatId(chatId);
    }

    public MessageEntity update(MessageEntity messageEntity){
        return repository.save(messageEntity);
    }
}
