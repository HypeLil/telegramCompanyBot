package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.User;
import com.nikita.telegramBot.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository repository;

    public User update(User user) {
        return repository.save(user);
    }

    public void deleteById(String id) {
        User user = get(id).get();
        repository.deleteById(id);
    }

    public User getOrCreate(String id) {
        return get(id).orElseGet(
                () -> update(new User(id, Role.USER)));
    }

    public Optional<User> get(String chatId) {
        return repository.findById(chatId);
    }
    public List<User> getUsers() {
        return repository.findAll();
    }

}
