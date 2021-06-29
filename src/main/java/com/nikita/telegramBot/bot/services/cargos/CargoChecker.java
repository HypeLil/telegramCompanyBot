package com.nikita.telegramBot.bot.services.cargos;

import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
@Deprecated
public class CargoChecker {

    private final Turkish turkish;
    private final China china;
    private final Europe europe;
    private final UserService userService;

    public SendMessage checker(Update update){
        String com = update.getMessage().getText().split(" ")[1];
        log.info("Запрос в чекере {}", com);

        if ("Турция".equalsIgnoreCase(com)){
            return turkish.start(update);
        }
        else if ("Китай".equalsIgnoreCase(com)){
            return china.start(update);
        }
        else if ("Европа".equalsIgnoreCase(com)){
           return europe.start(update);
        }
        log.error("В CargoChecker ошибка");
        return null;
    }

    public SendMessage cargo(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));

        String com = userEntity.getPosition().split("_")[1];
        log.info("Запрос в чекере {}", com);

        if ("turkish".equalsIgnoreCase(com)){
        }
        else if ("china".equalsIgnoreCase(com)){
        }
        else if ("europe".equalsIgnoreCase(com)){
            return europe.userAnswer(update);
        }
        log.error("В Cargo cargo ошибка");
        return null;
    }

}
