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

    public SendMessage setKeyboard(SendMessage sendMessage){
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

    public SendMessage cargo(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("cargo_order");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Доставим Ваш груз из любой страны мира! В нашем распоряжении все виды транспорта: автомобильный, морской, железнодорожный, авиационный.\n" +
                "Примерные сроки доставки: \n" +
                "•\tАвто из Европы - 7-14 дней\n" +
                "•\tАвто из Китая - 30 дней\n" +
                "•\tАвто из Турции - 14 дней\n" +
                "•\tМоре + Авто из Турции - 14 дней\n");

        return setKeyboard(sendMessage);
    }

    public SendMessage serviceDt(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("give_dt");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Оформим таможенную декларацию по Вашему товару. " +
                "\nПодберем код ТН ВЭД. Гарантируем сделать все, чтобы со стороны контролирующих органов не возникало дополнительных вопросов. " +
                "\nОформление ДТ возможно как под вашу ЭЦП, так и под печать брокера.");

        return setKeyboard(sendMessage);
    }
    public SendMessage findProvider(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("provider_find");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Найдем для вас поставщика в Китае. " +
                "\nВыступим посредником на каждом этапе: от проверки предлагаемого товара до заключения контракта и поставки на территорию России.");

        return setKeyboard(sendMessage);
    }
    public SendMessage surveer(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("surveer");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Проведем качественный анализ вашего будущего продавца. " +
                "\nВ собственности ли производственное помещение, устроены ли официально сотрудники, какова его история на рынке. " +
                "\nСюрвейерская проверка выявит все необходимые для заключения контракта детали.");

        return setKeyboard(sendMessage);
    }
    public SendMessage respKeeping(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("resp_keeping");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userEntity.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Возьмем на ответственное хранение ваш груз.\n" +
                "При необходимости, рассортируем, переупакуем, промаркируем, наклеим стикеры, проведем инвентаризацию и доставим груз до конечной точки.\n");

        return setKeyboard(sendMessage);
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
        keyboardThirdRow.add(new KeyboardButton("Сюрвейерская проверка"));

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add(new KeyboardButton("Поиск поставщика"));

        KeyboardRow keyboardSixRow = new KeyboardRow();
        keyboardSixRow.add(new KeyboardButton("Ответственное хранение"));

        KeyboardRow keyboardFiveRow = new KeyboardRow();
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
