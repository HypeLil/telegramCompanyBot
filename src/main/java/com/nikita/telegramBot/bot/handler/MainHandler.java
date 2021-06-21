package com.nikita.telegramBot.bot.handler;

import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.User;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class MainHandler {

    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private final UserService userService;

    @Value("${bot.number}")
    private String number;

    @Value("${bot.admin}")
    private String botAdmin;

    public SendMessage start(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        User user = userService.getOrCreate(chatId);

        if (botAdmin.equals(user.getUserId()) && user.getRole() != Role.ADMIN){
            user.setRole(Role.ADMIN);
            userService.update(user);
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        StringBuilder sb = new StringBuilder();
        sb.append("Приветствуем!\n" +
                "В нашем боте вы можете выбрать 1 из услуг:\n" +
                "- Импорт на прямой контракт\n" +
                "- ВЭД-сопровождение\n" +
                "- Таможенное оформление\n" +
                "- Сертификация\n" +
                "- Страхование\n" +
                "- Финансовое сопровождение\n" +
                "- Ответственное хранение\n" +
                "- Fashion маркировка\n" +
                "- Оптовая дистрибуция\n");
        sendMessage.setText(sb.toString());

        startMenu(sendMessage);

        return sendMessage;
    }

    public SendMessage aboutCompany(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        User user = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        user.setPosition("about_company");
        userService.update(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        String sb = "Free Lines Company – логистическая компания, осуществляющая перевозки партий коммерческих грузов из любой страны мира в любой город России, используя автомобильный, морской, авиационный и железнодорожный транспорт. \n" +
                "\n" +
                "Мы оперативно и качественно доставим Ваш груз с любого континента в любую точку России и окажем полный спектр услуг по его импортному оформлению и сертификации. \n" +
                "\n" +
                "Охват стран мира на сегодняшний день составляет 64 страны (это страны Европы, Азии, Северной и Южной Америки, Африки и Австралии). И количество стран постоянно растет!";
        sendMessage.setText(sb);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Подробнее"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Услуги"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("Готовые наборы"));

        KeyboardRow keyboardFiveRow = new KeyboardRow();
        keyboardFiveRow.add(new KeyboardButton("Назад"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFiveRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }

    public SendMessage startMenu(SendMessage sendMessage){
        User user = userService.getOrCreate(sendMessage.getChatId());

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER){
            KeyboardRow keyb = new KeyboardRow();
            keyb.add(new KeyboardButton("Админ-панель"));
            keyboard.add(keyb);
        }

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Каталог услуг"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Готовые наборы"));

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add((new KeyboardButton("Онлайн-чат")));
        keyboardFourthRow.add(new KeyboardButton("Позвонить"));

        KeyboardRow keyboardFiveRow = new KeyboardRow();
        keyboardFiveRow.add(new KeyboardButton("Информация о компании"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardFourthRow);
        keyboard.add(keyboardFiveRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }

    public SendMessage chat(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        sendMessage.setText("Для обращения в поддержку выберите 1 из вариантов:");

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Чат с менеджером");
        inlineKeyboardButton.setCallbackData("chat_with_manager");
        row1.add(inlineKeyboardButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Онлайн-форма");
        inlineKeyboardButton1.setCallbackData("online_form");
        row2.add(inlineKeyboardButton1);

        keyboard.add(row1);
        keyboard.add(row2);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }

    public SendMessage call(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        sendMessage.setText("Для вызова нажмите на номер:\n" +
                number);

        return sendMessage;
    }

    public SendMessage completedSets(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        User user = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        user.setPosition("completed_sets");
        userService.update(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Список готовых наборов можно увидеть в меню");

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Оптовая дистрибуция"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Комплексная доставка"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("Таможенное оформление"));

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add(new KeyboardButton("Проверенные поставщики"));

        KeyboardRow keyboardFiveRow = new KeyboardRow();
        keyboardFiveRow.add(new KeyboardButton("Назад"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);
        keyboard.add(keyboardFiveRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }

    public SendMessage startService(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        User user = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        user.setPosition("catalog");
        userService.update(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Каталог услуг:");

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Грузоперевозка Турция");
        inlineKeyboardButton.setCallbackData("turkish");
        row1.add(inlineKeyboardButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Грузоперевозка Китай");
        inlineKeyboardButton.setCallbackData("china");
        row2.add(inlineKeyboardButton1);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Грузоперевозка Европа");
        inlineKeyboardButton.setCallbackData("europe");
        row3.add(inlineKeyboardButton2);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Подробнее");
        inlineKeyboardButton.setCallbackData("moreInfo");
        row4.add(inlineKeyboardButton3);

        List<InlineKeyboardButton> row5 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Подача ДТ под печатью брокера");
        inlineKeyboardButton.setCallbackData("dt_broker");
        row5.add(inlineKeyboardButton4);

        List<InlineKeyboardButton> row6 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Подача ДТ под печатью клиента");
        inlineKeyboardButton.setCallbackData("dt_client");
        row6.add(inlineKeyboardButton5);

        List<InlineKeyboardButton> row7 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Сертификация");
        inlineKeyboardButton.setCallbackData("sertification");
        row7.add(inlineKeyboardButton6);

        List<InlineKeyboardButton> row8 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Поиск поставщика");
        inlineKeyboardButton.setCallbackData("find_provider");
        row8.add(inlineKeyboardButton7);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);
        keyboard.add(row7);
        keyboard.add(row8);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }
}
