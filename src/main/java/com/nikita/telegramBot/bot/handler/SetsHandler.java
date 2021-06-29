package com.nikita.telegramBot.bot.handler;

import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
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
public class SetsHandler {

    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final UserService userService;

    public SendMessage opt(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("opt");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Ваши заботы = наши задачи. Возьмем на себя под ключ:\n" +
                "•\tпоиск поставщика;\n" +
                "•\tсюрвейерскую проверку поставщика и товара;\n" +
                "•\tзаключим договор и оплатим товар;\n" +
                "•\tпривезем его в Россию;\n" +
                "•\tпроведем таможенную очистку;\n" +
                "•\tорганизуем складскую обработку товара в соответствии с требованиями маркетплейсов;\n" +
                "•\tдоставим товар на склады маркетплейсов.\n");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Заказать"));


        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage complex(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("complex");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Доставим Ваши грузы \"под ключ\":\n" +
                "•\tпривезем в Россию из любой страны мира любым доступным видом транспорта;\n" +
                "•\tсертифицируем;\n" +
                "•\tрастаможим;\n" +
                "•\tдоставим до пункта назначения.\n");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Заказать"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage customs(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("sets_custom");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Любой ввозимый на территорию РФ товар должен пройти таможенную очистку.\n" +
                "Мы \n" +
                "•\tподберем код ТН ВЭД с описанием;\n" +
                "•\tподадим ДТ под печать нашего брокера или клиента;\n" +
                "•\tсертифицируем товар.\n");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Заказать"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage provider(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("sets_provider");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("С комплексом \"Проверенный поставщик\" мы снимем заботы с Ваших плеч: \n" +
                "•\tнайдем поставщика и заключим с ним договор;\n" +
                "•\tпроведем сюрвейерскую проверку поставщика и товара;\n" +
                "•\tорганизуем ВЭД сопровождение.\n");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Заказать"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("Назад"));

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }
}
