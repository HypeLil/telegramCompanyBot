package com.nikita.telegramBot.repo;

import com.nikita.telegramBot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u.online=true")
    List<UserEntity> findAllOnline();

    @Query("SELECT u FROM UserEntity u WHERE u.online=true AND u.role='1'")
    List<UserEntity> findManagersOnline();
}
