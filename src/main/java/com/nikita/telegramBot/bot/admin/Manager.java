package com.nikita.telegramBot.bot.admin;

import com.nikita.telegramBot.model.ChatEntity;
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
        sb.append("Список диалогов\n(для выбора ответьте только цифрой без пробелов и прочего)\n");

        List<ChatEntity> chatEntities = chatService.findAllChatsByManagerId(managerId);

        if (chatEntities.isEmpty()){
            sb.append("Чатов нет");
            sendMessage.setText(sb.toString());
            return adminPanel.adminMenu(sendMessage, userEntity);
        }
        userEntity.setPosition("online_answers");
        userService.update(userEntity);

        for (int i = 0; i < chatEntities.size(); i++){
            UserEntity user = userService.getOrCreate(chatEntities.get(i).getUserId());
            sb.append("[").append(i).append("] Пользователь: ").append(user.getName()).append("Отвечено: ");
            if (chatEntities.get(i).getAnswered()){
                sb.append("Да");
            } else sb.append("Нет");
            sb.append("\n\n");
        }
        sendMessage.setText(sb.toString());

        return backButton(sendMessage);
    }

}
