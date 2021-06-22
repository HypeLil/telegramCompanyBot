package com.nikita.telegramBot.bot.admin;

import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.UserEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAdminPanel {

    public final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    public SendMessage adminMenu(SendMessage sendMessage, UserEntity userEntity){
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardOne = new KeyboardRow();
        keyboardOne.add(new KeyboardButton("Войти/Выйти из сети"));
        keyboard.add(keyboardOne);

        if (userEntity.getRole() == Role.MANAGER){
            KeyboardRow keyboardTwo = new KeyboardRow();
            keyboardTwo.add(new KeyboardButton("Список чатов"));

            KeyboardRow keyboardThree = new KeyboardRow();
            keyboardThree.add(new KeyboardButton("Выйти из админ-панели"));

            keyboard.add(keyboardTwo);
            keyboard.add(keyboardThree);
        }
        if (userEntity.getRole() == Role.ADMIN){
            KeyboardRow keyboardTwo = new KeyboardRow();
            keyboardTwo.add(new KeyboardButton("Администрация онлайн"));

            KeyboardRow keyboardThree = new KeyboardRow();
            keyboardThree.add(new KeyboardButton("Выдать права"));

            KeyboardRow keyboardFour = new KeyboardRow();
            keyboardFour.add(new KeyboardButton("Аналитика"));

            KeyboardRow keyboardFive = new KeyboardRow();
            keyboardFive.add(new KeyboardButton("Список чатов поддержки"));

            KeyboardRow keyboardSix = new KeyboardRow();
            keyboardSix.add(new KeyboardButton("Выйти из админ-панели"));

            keyboard.add(keyboardTwo);
            keyboard.add(keyboardThree);
            keyboard.add(keyboardFour);
            keyboard.add(keyboardFive);
            keyboard.add(keyboardSix);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return sendMessage;
    }
}
