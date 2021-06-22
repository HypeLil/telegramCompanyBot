package com.nikita.telegramBot.bot.handler;

import com.nikita.telegramBot.model.UserEntity;
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
@Slf4j
@RequiredArgsConstructor
public class ServiceCatalog {

    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final UserService userService;

    public SendMessage cargo(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("cargo");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Грузоперевозки:");

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFour = new KeyboardRow();
        keyboardFour.add(new KeyboardButton("Грузоперевозка Турция"));

        KeyboardRow keyboardThree = new KeyboardRow();
        keyboardThree.add(new KeyboardButton("Грузоперевозка Китай"));

        KeyboardRow keyboardSec = new KeyboardRow();
        keyboardSec.add(new KeyboardButton("Грузоперевозка Европа"));

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Назад"));

        keyboard.add(keyboardFour);
        keyboard.add(keyboardThree);
        keyboard.add(keyboardSec);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage serviceDt(Update update){

        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("dt");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Выберите:");

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardSec = new KeyboardRow();
        keyboardSec.add(new KeyboardButton("Подача ДТ под печатью клиента"));

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Подача ДТ под печатью брокера"));

        KeyboardRow keyboardThreeRow = new KeyboardRow();
        keyboardThreeRow.add(new KeyboardButton("Назад"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSec);
        keyboard.add(keyboardThreeRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage startService(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("catalog");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Каталог услуг:");

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Грузоперевозки"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Подача ДТ"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("Сертификация"));

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add(new KeyboardButton("Поиск поставщика"));

        KeyboardRow keyboardSixRow = new KeyboardRow();
        keyboardSixRow.add(new KeyboardButton("Таможенное оформление"));

        KeyboardRow keyboardFiveRow = new KeyboardRow();
        keyboardFiveRow.add(new KeyboardButton("Подробнее"));
        keyboardFiveRow.add(new KeyboardButton("Назад"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);
        keyboard.add(keyboardSixRow);
        keyboard.add(keyboardFiveRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }
}
