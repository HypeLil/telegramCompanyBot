package com.nikita.telegramBot.bot.admin;

import com.nikita.telegramBot.model.ChatEntity;
import com.nikita.telegramBot.model.MessageEntity;
import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.ChatService;
import com.nikita.telegramBot.service.MessageService;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Manager {

    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final AdminPanel adminPanel;
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

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

    public SendMessage listOfQuestions(Update update){
        String managerId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(managerId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(managerId);
        sendMessage.enableMarkdown(true);

        StringBuilder sb = new StringBuilder();
        sb.append("Список диалогов\n(для выбора ответьте только цифрой без пробелов и прочего)\n\n");

        List<ChatEntity> chatEntities = chatService.findAllChatsByManagerId(managerId);

        if (chatEntities.isEmpty()){
            sb.append("Чатов нет");
            sendMessage.setText(sb.toString());
            return adminPanel.adminMenu(sendMessage, userEntity);
        }

        for (ChatEntity chatEntity : chatEntities){
            UserEntity user = userService.getOrCreate(chatEntity.getUserId());
            sb.append(chatEntity.getChatId()).append(" - Пользователь: ").append(user.getName()).append(" | Отвечено: ");
            if (chatEntity.getAnswered()){
                sb.append("Да");
            } else sb.append("Нет");
            sb.append("\n\n");
        }

        userEntity.setPosition("online-answers");
        userService.update(userEntity);

        sendMessage.setText(sb.toString());

        return backButton(sendMessage);
    }

    public SendMessage enteredChat(Update update){
        String managerId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(managerId);
        String text = update.getMessage().getText();
        int chatId;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(managerId);
        sendMessage.enableMarkdown(true);

        try {
            chatId = Integer.parseInt(text);
        } catch (Exception e){
            sendMessage.setText("Вы ввели не число!");
            return sendMessage;
        }

        ChatEntity chatEntity = chatService.findByChatId(chatId);
        sendMessage.setText("Вы выбрали чат, выберите действие: \n" +
                "[1] - Ответить пользователю\n" +
                "[2] - Получить весь диалог");

        userEntity.setPosition("online-answers-entered:" + chatEntity.getChatId());
        userService.update(userEntity);

        return backButton(sendMessage);
    }

    public List<SendMessage> wholeDialogue(Update update){
        String managerId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(managerId);
        String text = update.getMessage().getText();
        UserEntity manager = userService.getOrCreate(managerId);
        int chatId = 0;

        try {
            chatId = Integer.parseInt(manager.getPosition().split(":")[1]);
        } catch (Exception e){
            log.error("Ошибка в диалоге при парсинге integer");
        }

        List<MessageEntity> messages = messageService.findAllByChatId(chatId);
        List<SendMessage> mess = new ArrayList<>();

        for (MessageEntity messageEntity : messages){
            StringBuilder sb = new StringBuilder();
            sb.append("Отправитель: ");

            if (messageEntity.getUserId().equals(managerId)){
                sb.append("Менеджер");
            } else sb.append("Пользователь");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            sb.append("\nВремя: ").append(messageEntity.getMessageTime().format(formatter));
            sb.append("\n").append(messageEntity.getText());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(managerId);
            sendMessage.enableMarkdown(true);
            sendMessage.setText(sb.toString());
            mess.add(sendMessage);
        }

        return mess;
    }

    public SendMessage answerChat(Update update){
        String managerId = String.valueOf(update.getMessage().getChatId());
        UserEntity userEntity = userService.getOrCreate(managerId);
        ChatEntity chatEntity = chatService.findByChatId(Integer.
                parseInt(userEntity.getPosition().split(":")[1]));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(managerId);
        sendMessage.enableMarkdown(true);

        userEntity.setPosition("online-answer-now:" + chatEntity.getChatId());
        userService.update(userEntity);
        sendMessage.setText("Введите ответ пользователю:");

        return backButton(sendMessage);
    }

    public List<SendMessage> answerMessageNow(Update update){
        String managerId = String.valueOf(update.getMessage().getChatId());
        UserEntity manager = userService.getOrCreate(managerId);
        int chatId = 0;

        try {
            chatId = Integer.parseInt(manager.getPosition().split(":")[1]);
        }catch (Exception e){
            log.error("exception");
        }
        ChatEntity chatEntity = chatService.findByChatId(chatId);
        String answer = update.getMessage().getText();
        String userId = chatEntity.getUserId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(managerId);
        sendMessage.enableMarkdown(true);

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChatId(chatEntity.getChatId());
        messageEntity.setMessageTime(LocalDateTime.now());
        messageEntity.setUserId(managerId);
        messageEntity.setText(answer);
        messageService.update(messageEntity);

        List<SendMessage> messages = new ArrayList<>();
        sendMessage.setText("Сообщение отправлено!");
        messages.add(sendMessage);

        chatEntity.setAnswered(true);
        chatService.update(chatEntity);

        sendMessage = new SendMessage();
        sendMessage.setChatId(userId);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Вам ответил менеджер: \n\n" + answer);
        messages.add(sendMessage);

        return messages;
    }

}
