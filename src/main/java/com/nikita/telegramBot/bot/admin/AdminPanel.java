package com.nikita.telegramBot.bot.admin;

import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminPanel extends AbstractAdminPanel{

    private final UserService userService;
    private final Admin admin;

    public SendMessage startAdmin(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        if (userEntity.getRole() == Role.USER){
            sendMessage.setText("Вы не админ!");
            log.error("Попытка входа в админ-панель без прав");
            return sendMessage;
        }

        sendMessage.setText("Админ-панель открыта");
        sendMessage.setReplyMarkup(super.replyKeyboardMarkup);

        return adminMenu(sendMessage, userEntity);
    }


    public SendMessage online(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setOnline(!userEntity.isOnline());
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        if (userEntity.isOnline()){
            sendMessage.setText("Вы вошли в сеть");
        } else sendMessage.setText("Вы вышли из сети");

        return adminMenu(sendMessage, userEntity);
    }
    public SendMessage whoIsOnline(Update update){
        return admin.whoIsOnline(update);
    }
    public SendMessage issuePermissions(Update update){
        return admin.issuePermissions(update);
    }
    public SendMessage issuePermissionsPartTwo(Update update){
        return admin.issuePermissionPartTwo(update);
    }
    public SendMessage issuePermissionsPartThree(Update update){
        return admin.issuePermissionPartThree(update);
    }
    public List<SendMessage> issuePermissionsPartFour(Update update){
        return admin.issuePermissionPartFour(update);
    }
    public SendMessage listOfQuestions(Update update){
        return admin.listOfQuestions(update);
    }
    public SendMessage dialogueWithUser(Update update){
        return admin.dialogueWithUser(update);
    }
    public SendMessage listOfManagers(Update update){
        return admin.listOfManagers(update);
    }
    public SendMessage aboutUser(Update update){
        return admin.aboutUser(update);
    }
    public List<SendMessage> dialogueOfManager(Update update){
        return admin.dialogueOfManager(update);
    }
    public SendMessage analytics(Update update){
        return admin.analytics(update);
    }
    public SendMessage suggestDate(Update update){
        return admin.suggestDate(update);
    }
    public SendMessage resultAnalytics(Update update){
        return admin.resultAnalytics(update);
    }

}
