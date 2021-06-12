package com.nikita.telegramBot.bot.services.cargos;

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
@Slf4j
@RequiredArgsConstructor
public class Europe implements Cargo{

    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final UserService userService;

    private String[] countries = new String[]{"0", "Эстония", "Швеция", "Швейцария", "Чехия",
            "Хорватия", "Финляндия", "Словения", "Словакия",
            "Сербия", "Румыния", "Португалия", "Норвегия", "Норвегия", "Польша",
            "Нидерланды", "Люксембург", "Литва", "Италия"};

    @Override
    public SendMessage start(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        User user = userService.getOrCreate(chatId);
        user.setPosition("carg_europe");
        userService.update(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);

        StringBuilder sb = new StringBuilder();
        sb.append("Выберите доставку грузов из нужной страны (Введите только цифру без пробела):\n" +
        "[1] Эстония\n" +
                "[2] Швеция\n" +
                "[3] Швейцария\n" +
                "[4] Чехия\n" +
                "[5] Хорватия\n" +
                "[6] Финляндия\n" +
                "[7] Словения\n" +
                "[8] Словакия\n" +
                "[9] Сербия\n" +
                "10 Румыния\n" +
                "[11] Португалия\n" +
                "[12] Норвегия\n" +
                "[13] Польша\n" +
                "[14] Нидерланды\n" +
                "[15] Люксембург\n"+
                "[16] Литва \n" +
                "[17] Италия");

        sendMessage.setText(sb.toString());

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Назад"));
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

    public SendMessage userAnswer(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        User user = userService.getOrCreate(chatId);
        int ans = 0;

        String answer = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            ans = Integer.parseInt(answer);
        }catch (Exception e){
            sendMessage.setText("Вы должны ввести число от 1 до 17 включительно");
            return sendMessage;
        }

        if (ans < 1 || ans > 17){
            sendMessage.setText("Вы должны ввести число от 1 до 17 включительно");
            return sendMessage;
        }
        sendMessage.setText("Вы выбрали " + countries[ans-1]);
        user.setPosition("заказ_" + countries[ans-1].toString());
        userService.update(user);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Подробнее"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Назад");

        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return sendMessage;
    }

}
