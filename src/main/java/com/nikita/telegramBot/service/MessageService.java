package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.MessageEntity;
import com.nikita.telegramBot.repo.JpaMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final JpaMessageRepository repository;

    public List<MessageEntity> findAllByChatId(int chatId){
        return repository.findAllByChatId(chatId);
    }

    public MessageEntity update(MessageEntity messageEntity){
        return repository.save(messageEntity);
    }
}
