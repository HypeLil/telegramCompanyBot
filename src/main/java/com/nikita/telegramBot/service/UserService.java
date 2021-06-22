package com.nikita.telegramBot.service;

import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository repository;

    public UserEntity update(UserEntity userEntity) {
        if (userEntity.getUserId() == null){
            log.error("Id равен null");
        }
        return repository.save(userEntity);
    }

    public UserEntity getOrCreate(String id) {
        return get(id).orElseGet(
                () -> update(new UserEntity(id)));
    }

    private Optional<UserEntity> get(String chatId) {
        return repository.findById(chatId);
    }
    public List<UserEntity> whoIsOnline(){return repository.findAllOnline();}
    public List<UserEntity> findManagersOnline(){ return repository.findManagersOnline(); }
    public List<UserEntity> findManagers(){
        return repository.findManagers();
    }
    public List<UserEntity> findAllUsersNotAdmins(){
        return repository.findAllUsersNotAdmins();
    }

}
