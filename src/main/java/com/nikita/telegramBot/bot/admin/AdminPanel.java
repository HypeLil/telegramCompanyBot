package com.nikita.telegramBot.bot.admin;

import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.User;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminPanel {

    private final UserService userService;
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public SendMessage startAdmin(Update update){
        User user = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        if (user.getRole() == Role.USER){
            sendMessage.setText("Вы не админ!");
            log.error("Попытка входа в админ-панель без прав");
            return sendMessage;
        }

        sendMessage.setText("Админ-панель открыта");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardOne = new KeyboardRow();
        if (user.isOnline()) {
            keyboardOne.add(new KeyboardButton("Выйти из сети"));
        } else {
            keyboardOne.add(new KeyboardButton("Войти в сеть"));
        }
        keyboard.add(keyboardOne);

        if (user.getRole() == Role.MANAGER){
            KeyboardRow keyboardTwo = new KeyboardRow();
            keyboardTwo.add(new KeyboardButton("Список чатов без ответа"));

            KeyboardRow keyboardThree = new KeyboardRow();
            keyboardThree.add(new KeyboardButton("Выйти из админ-панели"));

            keyboard.add(keyboardTwo);
            keyboard.add(keyboardThree);
        }
        if (user.getRole() == Role.ADMIN){
            KeyboardRow keyboardTwo = new KeyboardRow();
            keyboardTwo.add(new KeyboardButton("Менеджеры онлайн"));

            KeyboardRow keyboardThree = new KeyboardRow();
            keyboardThree.add(new KeyboardButton("Выдать права"));

            KeyboardRow keyboardFour = new KeyboardRow();
            keyboardFour.add(new KeyboardButton("Аналитика"));

            KeyboardRow keyboardFive = new KeyboardRow();
            keyboardFive.add(new KeyboardButton("Список чатов"));

            KeyboardRow keyboardSix = new KeyboardRow();
            keyboardSix.add(new KeyboardButton("Выйти из админ-панели"));

            keyboard.add(keyboardTwo);
            keyboard.add(keyboardThree);
            keyboard.add(keyboardFour);
            keyboard.add(keyboardFive);
            keyboard.add(keyboardSix);
        }

        return sendMessage;
    }

}
