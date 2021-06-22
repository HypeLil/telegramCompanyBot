package com.nikita.telegramBot.repo;

import com.nikita.telegramBot.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMessageRepository extends JpaRepository<MessageEntity, Integer> {

    @Query("SELECT m FROM MessageEntity m WHERE m.chatId=:chat")
    public List<MessageEntity> findAllByChatId(@Param("chat") String chatId);
}
