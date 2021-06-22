package com.nikita.telegramBot.bot.admin;

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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class Admin extends AbstractAdminPanel{

    @Value("${bot.admin}")
    private String botAdmin;

    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;
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
        if (userEntity.getUserId().equals(botAdmin)){
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

    public SendMessage listOfManagers(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        List<UserEntity> userEntities = userService.findManagers();
        StringBuilder sb = new StringBuilder();

        if (userEntities.isEmpty()){
            sendMessage.setText("Менеджеров нет");
            return sendMessage;
        }

        sb.append("Выберите нужного менеджера:\n").append("(Только цифра)\n");
        for (int i = 0; i < userEntities.size(); i++){
            sb.append("\n[").append(i).append("]").append(" - ").append(userEntities.get(i).getName());
        }
        sendMessage.setText(sb.toString());
        admin.setPosition("entered-manager");
        userService.update(admin);

        return adminMenu(sendMessage, admin);
    }

    public SendMessage listOfQuestions(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String text = update.getMessage().getText();
        int managerId;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        try {
           managerId = Integer.parseInt(text);
        }catch (Exception e){
            sendMessage.setText("Либо ввели не цифру, либо такого числа не было в списке");
            return adminMenu(sendMessage, admin);
        }

        List<UserEntity> userEntities = userService.findManagers();
        UserEntity manager = userEntities.get(managerId);

        List<ChatEntity> chatEntities = chatService.findAllChatsByManagerId(manager.getUserId());

        if (chatEntities != null && chatEntities.isEmpty()){
            sendMessage.setText("У данного менеджера нет диалогов");
            return adminMenu(sendMessage, admin);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Список диалогов:\n");
        for (ChatEntity chatEntity : chatEntities){
            sb.append("\n[").append(chatEntity.getChatId()).append("] - Диалог с ").append(userService.getOrCreate(chatEntity.getUserId()).getName());
        }
        sendMessage.setText(sb.toString());
        admin.setPosition("entered-manager:" + manager.getUserId());
        userService.update(admin);

        return adminMenu(sendMessage, admin);
    }

    public SendMessage dialogueWithUser(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String text = update.getMessage().getText();
        int chatId = 0;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

            try {
                chatId = Integer.parseInt(text);
            }catch (Exception e){
                if (!"О пользователе".equals(update.getMessage().getText()) || !"Диалог менеджера".equals(update.getMessage().getText())) {
                    sendMessage.setText("Либо ввели не цифру, либо такого числа не было в списке");
                    return adminMenu(sendMessage, admin);
                }
            }

        sendMessage.setText("Выберите действия");
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("О пользователе"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Диалог менеджера"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Назад"));

        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        admin.setPosition("entered-manager:" + admin.getPosition().split(":")[1] + ":" + chatId);
        userService.update(admin);

        return sendMessage;
    }

    public SendMessage aboutUser(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        UserEntity user = userService.getOrCreate(admin.getPosition().split(":")[2]);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableMarkdown(true);

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sb.append("Данные пользователя: \n").append("ФИО: ").append(user.getName())
                .append("\nНомер: ").append(user.getNumber())
                .append("\nПочта: ").append(user.getEmail())
                .append("\nПоследние действия: ").append(user.getLastAction().format(formatter));

        sendMessage.setText(sb.toString());
        admin.setPosition("start");
        userService.update(admin);

        return adminMenu(sendMessage, admin);
    }

    public List<SendMessage> dialogueOfManager(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));

        List<MessageEntity> messages = messageService.findAllByChatId(Integer.parseInt(admin.getPosition().split(":")[2]));
        List<SendMessage> mess = new ArrayList<>();

        for (MessageEntity messageEntity : messages){
            StringBuilder sb = new StringBuilder();
            sb.append("Отправитель: ");

            if (messageEntity.getUserId().equals(admin.getPosition().split(":")[1])){
                sb.append("Менеджер");
            } else sb.append("Пользователь");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            sb.append("\nВремя: ").append(messageEntity.getMessageTime().format(formatter));
            sb.append("\n").append(messageEntity.getText());

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(admin.getUserId());
            sendMessage.enableMarkdown(true);
            sendMessage.setText(sb.toString());
            mess.add(sendMessage);
        }
        return mess;
    }

    public SendMessage analytics(Update update){
        StringBuilder sb = new StringBuilder();
        sb.append("Кол-во взаимодействий пользователей с ботами сегодня: ");
        List<UserEntity> userEntities = userService.findAllUsersNotAdmins();

        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        admin.setPosition("admin_analytics");
        userService.update(admin);

        int countActions = 0;
        for (UserEntity userEntity : userEntities){
            if (userEntity.getLastAction().isEqual(LocalDateTime.now())){
                countActions++;
            }
        }
        sb.append(countActions);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Кол-во диалогов"));

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Назад"));

        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(admin.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(sb.toString());

        return sendMessage;
    }

    public SendMessage suggestDate(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        admin.setPosition("admin_suggest");
        userService.update(admin);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(admin.getUserId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Введите нужный промежуток\n" +
                "Пример: 2021 4 30 - 2021 5 10");

        return backButton(sendMessage);
    }

    public SendMessage resultAnalytics(Update update){
        UserEntity admin = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String position = admin.getPosition();
        admin.setPosition("start");
        userService.update(admin);

        String[] dates = position.split(" - ");
        LocalDate startDate = LocalDate.of(Integer.parseInt(dates[0].split(" ")[0]), Integer.parseInt(dates[0].split(" ")[1]),
                Integer.parseInt(dates[0].split(" ")[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(dates[1].split(" ")[0]), Integer.parseInt(dates[1].split(" ")[1]),
                Integer.parseInt(dates[1].split(" ")[2]));

        LocalDateTime startLocalDate = LocalDateTime.of(startDate, LocalTime.now());
        LocalDateTime endLocalDate = LocalDateTime.of(endDate, LocalTime.now());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(admin.getUserId());
        sendMessage.enableMarkdown(true);

        List<ChatEntity> chatEntities = chatService.getBetweenHalfOpen(startLocalDate, endLocalDate);
        StringBuilder sb = new StringBuilder();

        if (chatEntities.isEmpty()){
            sb.append("Диалогов в заданный промежуток времени нет");
            sendMessage.setText(sb.toString());
            return adminMenu(sendMessage, admin);
        }
        int countChat = chatEntities.size();

        sb.append("Кол-во диалогов начиная с ").append(startDate).append(" заканчивая ")
                .append(endDate).append(" = ").append(countChat);

        return adminMenu(sendMessage, admin);
    }


}
