package com.nikita.telegramBot.bot.handler;

import com.nikita.telegramBot.model.ChatEntity;
import com.nikita.telegramBot.model.MessageEntity;
import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.ChatService;
import com.nikita.telegramBot.service.MessageService;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class MainHandler {

    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;

    @Value("${bot.number}")
    private String number;

    @Value("${bot.admin}")
    private String botAdmin;

    public SendMessage start(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(chatId);

        if (botAdmin.equals(userEntity.getUserId()) && userEntity.getRole() != Role.ADMIN){
            userEntity.setRole(Role.ADMIN);
            userService.update(userEntity);
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
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("about_company");
        userService.update(userEntity);

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
        UserEntity userEntity = userService.getOrCreate(sendMessage.getChatId());

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        if (userEntity.getRole() == Role.ADMIN || userEntity.getRole() == Role.MANAGER){
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
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("completed_sets");
        userService.update(userEntity);

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
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("catalog");
        userService.update(userEntity);

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

    public SendMessage backButton(SendMessage sendMessage){
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Назад"));

        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage startOnlineChat(Update update){
        List<UserEntity> managers = userService.findManagersOnline();
        String chatId = String.valueOf(update.getCallbackQuery().getFrom().getId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        if (managers.isEmpty()){
            sendMessage.setText("Извините, нет менеджеров в сети :(");
            return startMenu(sendMessage);
        }
        UserEntity user = userService.getOrCreate(chatId);
        user.setPosition("online_chat");
        userService.update(user);

        if (chatService.findChatEntityByUserId(chatId) == null){
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setUserId(chatId);
            chatEntity.setAnswered(true);
            int randomManager = (int)(Math.random() * managers.size());
            UserEntity userEntity = managers.get(randomManager);
            chatEntity.setManagerId(userEntity.getUserId());
            chatService.update(chatEntity);
        }

        sendMessage.setText("Здравствуйте! Напишите ваш вопрос");

        return backButton(sendMessage);
    }

    public List<SendMessage> onlineChat(Update update){
        String chatId = String.valueOf(update.getMessage().getChatId());
        ChatEntity chatEntity = chatService.findChatEntityByUserId(chatId);
        String question = update.getMessage().getText();

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChatId(chatEntity.getChatId());
        messageEntity.setUserId(chatId);
        messageEntity.setMessageTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        messageEntity.setText(question);
        messageService.update(messageEntity);

        List<SendMessage> messages = new ArrayList<>();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Сообщение отправлено менеджеру!");
        messages.add(startMenu(sendMessage));

        UserEntity userEntity = userService.getOrCreate(chatId);
        userEntity.setPosition("start");
        userService.update(userEntity);

        chatEntity.setAnswered(false);
        chatEntity.setLastMessage(messageEntity.getMessageTime());
        chatService.update(chatEntity);

        sendMessage = new SendMessage();
        sendMessage.setChatId(chatEntity.getManagerId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Новый запрос в поддержку!");
        messages.add(sendMessage);

        return messages;
    }
}
