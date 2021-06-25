package com.nikita.telegramBot.bot.handler;


import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import com.nikita.telegramBot.util.mail.EmailSender;
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
public class OrderHandler {

    private final UserService userService;
    private final MainHandler mainHandler;
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final String answer = "Заявка отправлена! С Вами скоро свяжутся";
    private String orderPosition;

    private String orderType() {
        String position = orderPosition;
        String order = "";
     /*
        if ("china_sky".equalsIgnoreCase(position)) {
            order = "Авиадоставка из Китая";
        } else if ("china_auto".equalsIgnoreCase(position)) {
            order = "Автоперевозки из Китая";
        } else if ("china_train".equalsIgnoreCase(position)) {
            order = "Железнодорожные перевозки из Китая";
        } else if ("turkish_sky".equalsIgnoreCase(position)) {
            order = "Авиадоставка грузов из Турции";
        } else if ("turkish_auto".equalsIgnoreCase(position)) {
            order = "Автомобильные перевозки грузов из Турции";
        } else if ("turkish_multi".equalsIgnoreCase(position)) {
            order = "Мультимодальные перевозки грузов из Турции";
        } else if ("custom".equalsIgnoreCase(position)) {
            order = "Таможенное оформление";
        } else if ("provider".equalsIgnoreCase(position)) {
            order = "Поиск поставщика";
        } else if ("dt-service_client".equalsIgnoreCase(position)) {
            order = "ДТ под печатью клиента";
        } else if ("dt-service_broker".equalsIgnoreCase(position)) {
            order = "ДТ под печатью брокера";
        } else if ("заказ".equalsIgnoreCase(position.split("_")[0])){
            order = "Доставка грузов из " + position.split("_")[1];
        }

      */
        if ("opt".equalsIgnoreCase(position)){
            order = "Оптовая дистрибуция";
        }
        else if ("complex".equalsIgnoreCase(position)){
            order = "Комплексная доставка";
        }
        else if ("sets_custom".equalsIgnoreCase(position)){
            order = "Таможенное оформление";
        }
        else if ("sets_provider".equalsIgnoreCase(position)){
            order = "Проверенные поставщики";
        }
        else if ("cargo_order".equals(position)){
            order = "Грузоперевозки";
        }
        else if ("give_dt".equals(position)){
            order = "Подача ДТ";
        }
        else if ("provider_find".equals(position)){
            order = "Поиск поставщика";
        }
        else if ("surveer".equals(position)){
            order = "Сюрвеерская проверка";
        }
        else if ("resp_keeping".equals(position)){
            order = "Ответственное хранение";
        }
        else log.error("Такого вида заказа нет");

        return order;
    }

    public SendMessage order(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String orderPosition = userEntity.getPosition();
        userEntity.setOrderPosition(orderPosition);

        String message = "Введите ФИО";
        userEntity.setPosition("enter_name");
        userService.update(userEntity);
        log.info("Ввод фио");

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);

        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Назад"));
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setText(message);

        return sendMessage;
    }

    public SendMessage enterName(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String name = update.getMessage().getText();

        String message = "Введите номер";
        userEntity.setPosition("enter_number");
        userEntity.setName(name);
        log.info("Ввод номера");

        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);

        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Назад"));
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        keyboardRowList.add(keyboardRow);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setText(message);

        return sendMessage;
    }

    public SendMessage result(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String number = update.getMessage().getText();

        userEntity.setPosition("start");
        userEntity.setNumber(number);
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(answer);

        StringBuilder sb = new StringBuilder();
        sb.append(" ФИО: ").append(userEntity.getName());
        sb.append("\n Номер: ").append(userEntity.getNumber());

        orderPosition = userEntity.getOrderPosition();
        EmailSender.send(sb.toString(), orderType());

        return mainHandler.startMenu(sendMessage);
    }
}
