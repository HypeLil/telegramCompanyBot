package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.ChatEntity;
import com.nikita.telegramBot.repo.JpaChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final JpaChatRepository repository;

    public List<ChatEntity> findAllChatsByManagerId(String managerId){
        return repository.findAllChatsByManagerId(managerId);
    }

    public ChatEntity findChatEntityByUserIdAndManagerId(String userId, String managerId){
        return repository.findChatEntityByUserIdAndManagerId(userId,managerId);
    }

    public ChatEntity update(ChatEntity chatEntity){
        return repository.save(chatEntity);
    }

    public ChatEntity findChatEntityByUserId(String userId){
        return repository.findChatEntityByUserId(userId);
    }

    public ChatEntity findByChatId(int chatId){
        return repository.findById(chatId).get();
    }
}
