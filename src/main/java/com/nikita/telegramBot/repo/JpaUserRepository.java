package com.nikita.telegramBot.repo;

import com.nikita.telegramBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.online=true")
    List<User> findAllOnline();
}
