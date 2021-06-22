package com.nikita.telegramBot.bot.admin;

import com.nikita.telegramBot.model.Role;
import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class Admin {

    @Value("${bot.admin}")
    private String botAdmin;

    private final UserService userService;
    private final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public SendMessage whoIsOnline(Update update){
        List<UserEntity> onlineUserEntities = userService.whoIsOnline();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        StringBuilder sb = new StringBuilder();
        sb.append("Онлайн:\n");
        for (UserEntity u : onlineUserEntities){
            String role =  u.getRole() == Role.MANAGER ? "Менеджер" : "Администратор";
            if (u.getUserId().equals(botAdmin)){

                role = "Главный администратор";
            }

            sb.append(u.getName()).append(" - ").append(role).append("\n");
        }
        sendMessage.setText(sb.toString());
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

    public SendMessage issuePermissions(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        userEntity.setPosition("admin_permission");
        userService.update(userEntity);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

       StringBuilder sb = new StringBuilder();
        if (!userEntity.getUserId().equals(botAdmin)){
            sb.append("Вы можете назначать менеджеров и админов");
        } else  sb.append("Вы можете назначать только менеджеров");

        sb.append("\nВведите id пользователя");
        sendMessage.setText(sb.toString());

        return backButton(sendMessage);
    }

    public SendMessage issuePermissionPartTwo(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String newAdminId = update.getMessage().getText();
        int checkCorrectNewAdminId = 0;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        try {
            checkCorrectNewAdminId = Integer.parseInt(newAdminId);
        }catch (Exception e){
            sendMessage.setText("Вы ввели некорректный id");
            return sendMessage;
        }

        userEntity.setPosition("admin_permission-role:" + newAdminId);
        userService.update(userEntity);

        sendMessage.setText("Выберите выдаваемые права:\n" +
                "1 - Менеджер\n" +
                "2 - Админ");

        return backButton(sendMessage);
    }

    public SendMessage issuePermissionPartThree(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String newAdminId = userEntity.getPosition().split(":")[1];
        String text = update.getMessage().getText();
        String role = "";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        if ("1".equals(text)){
            role = "manager";
        }
        else if ("2".equals(text)){
            role = "admin";
        }
        else {
            sendMessage.setText("Некорректный выбор, надо 1 или 2");
            return sendMessage;
        }

        userEntity.setPosition("admin_permission-name:" + newAdminId +":" + role);
        userService.update(userEntity);

        sendMessage.setText("Введите ФИО сотрудника:");

        return backButton(sendMessage);
    }

    public List<SendMessage> issuePermissionPartFour(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String newAdminId = userEntity.getPosition().split(":")[1];
        String role = userEntity.getPosition().split(":")[2];
        String name = update.getMessage().getText();

        UserEntity newAdmin = userService.getOrCreate(newAdminId);
        newAdmin.setRole(role.equals("manager") ? Role.MANAGER : Role.ADMIN);
        newAdmin.setName(name);
        userService.update(newAdmin);

        List<SendMessage> messages = new ArrayList<>();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        sendMessage.setText("Вы выдали права " + newAdmin.getName());
        messages.add(sendMessage);

        sendMessage = new SendMessage();
        sendMessage.setChatId(newAdminId);
        sendMessage.enableMarkdown(true);


        StringBuilder sb = new StringBuilder();
        sb.append("Вам выдали права ");
        if (newAdmin.getRole() == Role.ADMIN){
            sb.append("Администратора");
        } else sb.append("Менеджера");
        sendMessage.setText(sb.toString());
        messages.add(sendMessage);

        return messages;
    }
}
