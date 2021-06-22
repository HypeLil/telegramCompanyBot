package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.User;
import com.nikita.telegramBot.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository repository;

    public User update(User user) {
        if (user.getUserId() == null){
            log.error("Id равен null");
        }
        return repository.save(user);
    }

    public User getOrCreate(String id) {
        return get(id).orElseGet(
                () -> update(new User(id)));
    }

    public Optional<User> get(String chatId) {
        return repository.findById(chatId);
    }
    public List<User> whoIsOnline(){return repository.findAllOnline();}

}
