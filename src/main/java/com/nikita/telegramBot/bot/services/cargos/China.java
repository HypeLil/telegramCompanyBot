package com.nikita.telegramBot.bot.services.cargos;

import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class China implements Cargo{

    private InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final UserService userService;

    @Override
    public SendMessage start(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("china");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Грузоперевозка Китай:");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Автоперевозки из Китая"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Железнодорожные перевозки из Китая"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Авиадоставка из Китая"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage sky(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("china_sky");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Компания Free Lines из года в год усовершенствует все виды оказываемых услуг, и, особенно, " +
                "перевозки из Китая в Россию. \nЗа время работы мы наладили сотрудничество с самыми надежными авиаперевозчиками," +
                " чтобы максимально сократить срок доставки Ваших грузов. \nНа данный момент можем гарантировать," +
                " что товар из любого аэропорта Китая в любой аэропорт Москвы будет доставлен в течение 3 - 5 дней.");
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

        return sendMessage;
    }

    public SendMessage auto(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("china_auto");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Самый удобный и часто используемый способ доставки из Китая осуществляется автомобильным транспортом." +
                " \nБольшой плюс – отсутствие расписания, то есть отдельное авто может выехать " +
                "в любой день и время, без привязки к морским линиям или железнодорожным операторам.");
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

        return sendMessage;
    }

    public SendMessage train(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("china_train");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Грузоперевозки из Китая являются флагманским направлением компании Free Lines. \nБлагодаря прямым " +
                "поездам железнодорожные перевозки из " +
                "Китая стали одним из самых оперативных и надёжных способов доставки в Россию.");
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

        return sendMessage;
    }
}
