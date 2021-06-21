package com.nikita.telegramBot.repo;

import com.nikita.telegramBot.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, String> {

    @Override
    <S extends User> S save(S s);

    @Override
    void deleteById(String s);

    @Override
    Optional<User> findById(String s);

    @Override
    <S extends User> List<S> findAll(Example<S> example);

    @Query("SELECT u FROM User u WHERE u.online=true")
    List<User> findAllOnline();
}
