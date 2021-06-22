package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.ChatEntity;
import com.nikita.telegramBot.repo.JpaChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final JpaChatRepository repository;

    public List<ChatEntity> findAllChatsByManagerId(String managerId){
        return repository.findAllChatsByManagerId(managerId);
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

    public void delete(List<ChatEntity> chatEntities){
        repository.deleteAll(chatEntities);
    }

    public List<ChatEntity> getBetweenHalfOpen(LocalDateTime start,LocalDateTime end){
        return repository.getBetweenHalfOpen(start, end);
    }
}
