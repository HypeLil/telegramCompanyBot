package com.nikita.telegramBot.bot.services;

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
@Deprecated
public class ServiceDT {

    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final UserService userService;

    public SendMessage start(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("dt-service");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Выберите услугу:");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Подача ДТ под печатью брокера"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Подача ДТ под печатью клиента"));


        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage client(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("dt-service_client");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Для того, чтобы товар смог поступить в свободное обращение на территории Российской Федерации ему нужно пройти таможенную очистку. " +
                "\nОна включает в себя уплату пошлин, сборов и НДС, в соответствии заявляемой номенклатурой внешнеэкономической деятельности.");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Подробнее"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Заказать"));


        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage broker(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("dt-service_broker");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Для того, чтобы товар смог поступить в свободное обращение на территории Российской Федерации ему нужно пройти таможенную очистку. " +
                "\nОна включает в себя уплату пошлин, сборов и НДС, в соответствии заявляемой номенклатурой внешнеэкономической деятельности.");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Подробнее"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Заказать"));


        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }
}
