package com.nikita.telegramBot.repo;

import com.nikita.telegramBot.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaChatRepository extends JpaRepository<ChatEntity, Integer> {

    @Query("SELECT c FROM ChatEntity c WHERE c.managerId=:manager")
    List<ChatEntity> findAllChatsByManagerId(@Param("manager") String managerId);

    ChatEntity findChatEntityByUserIdAndManagerId(String userId, String managerId);

    ChatEntity findChatEntityByUserId(String userId);

    @Query("SELECT c FROM ChatEntity c WHERE c.lastMessage>=:start AND c.lastMessage<=:end")
    List<ChatEntity> getBetweenHalfOpen(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
