package com.nikita.telegramBot.repo;

import com.nikita.telegramBot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u.online=true")
    List<UserEntity> findAllOnline();
}
