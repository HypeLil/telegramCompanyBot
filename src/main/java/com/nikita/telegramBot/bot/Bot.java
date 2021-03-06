package com.nikita.telegramBot.bot;

import com.nikita.telegramBot.bot.admin.AdminPanel;
import com.nikita.telegramBot.bot.admin.Manager;
import com.nikita.telegramBot.bot.handler.OrderHandler;
import com.nikita.telegramBot.bot.handler.SetsHandler;
import com.nikita.telegramBot.bot.services.cargos.CargoChecker;
import com.nikita.telegramBot.bot.handler.MainHandler;
import com.nikita.telegramBot.bot.handler.ServiceCatalog;
import com.nikita.telegramBot.model.UserEntity;
import com.nikita.telegramBot.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@ComponentScan(basePackages = "application.yaml")
@Slf4j
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    @Getter
    private String token;

    @Value("${bot.name}")
    @Getter
    private String name;

    private final MainHandler mainHandler;
    private final UserService userService;
    private final ServiceCatalog serviceCatalog;
    private final CargoChecker cargoChecker;
    private final SetsHandler setsHandler;
    private final OrderHandler orderHandler;
    private final AdminPanel adminPanel;
    private final Manager manager;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
            userEntity.setLastAction(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
            userService.update(userEntity);
            String command = update.getMessage().getText();

            if ("??????????".equalsIgnoreCase(command)){
                executeMessage(back(update));
                return;
            }

            // ???????????????? ??????????????
            if (!userEntity.getPosition().equalsIgnoreCase("start")){
                String position = userEntity.getPosition();
                if ("enter_name".equalsIgnoreCase(position)){
                    executeMessage(orderHandler.enterName(update));
                }
                else if ("enter_number".equalsIgnoreCase(position)){
                    executeMessage(orderHandler.result(update));
                }
                else if ("enter_email".equalsIgnoreCase(position)){
                    executeMessage(orderHandler.result(update));
                }
                else if ("online_chat".equalsIgnoreCase(position)){
                    mainHandler.onlineChat(update).forEach(this::executeMessage);
                }
                else if ("admin_permission".equalsIgnoreCase(position)){
                    executeMessage(adminPanel.issuePermissionsPartTwo(update));
                }
                else if ("online-answers".equalsIgnoreCase(position)){
                    executeMessage(manager.enteredChat(update));
                }
                else if ("admin_suggest".equalsIgnoreCase(position)){
                    executeMessage(adminPanel.resultAnalytics(update));
                }
                else if ("entered-manager".equalsIgnoreCase(position)){
                    executeMessage(adminPanel.listOfQuestions(update));
                }
                else if (position.split(":").length > 1 && "entered-manager".equals(position.split(":")[0])){
                    executeMessage(adminPanel.dialogueWithUser(update));
                }
                else if (position.split(":").length > 1 && "online-answers-entered".equalsIgnoreCase(position.split(":")[0])){
                    String text = update.getMessage().getText();
                    if ("1".equals(text)){
                        executeMessage(manager.answerChat(update));
                    } else if ("2".equals(text)){
                        manager.wholeDialogue(update).forEach(this::executeMessage);
                    }
                }
                else if (position.split(":").length > 1 && "online-answer-now".equalsIgnoreCase(position.split(":")[0])){
                    manager.answerMessageNow(update).forEach(this::executeMessage);
                }
                else if (position.split(":").length > 1 && "admin_permission-role".equalsIgnoreCase(position.split(":")[0])){
                    executeMessage(adminPanel.issuePermissionsPartThree(update));
                }
                else if (position.split(":").length > 1 && "admin_permission-name".equalsIgnoreCase(position.split(":")[0])){
                    adminPanel.issuePermissionsPartFour(update).forEach(this::executeMessage);
                }
            }

            if ("carg".equalsIgnoreCase(userEntity.getPosition().split("_")[0]) && !"??????????".equalsIgnoreCase(command)){
                executeMessage(cargoChecker.cargo(update));
            }
                userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
            if ("/start".equalsIgnoreCase(command)) {
                executeMessage(mainHandler.start(update));
            }
            else if ("???????????????????? ?? ????????????????".equalsIgnoreCase(command)){
                executeMessage(mainHandler.aboutCompany(update));
            }
            else if ("????????????-??????".equalsIgnoreCase(command)){
                executeMessage(mainHandler.chat(update));
            }
            else if ("??????????????????".equalsIgnoreCase(command)){
                executeMessage(mainHandler.call(update));
            }
            else if ("?????????????? ????????????".equalsIgnoreCase(command)){
                executeMessage(mainHandler.completedSets(update));
            }
            else if ("?????????????? ??????????".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.startService(update));
            }
            else if ("????????????????????????????".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.cargo(update));
            }
            else if ("???????????? ????".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.serviceDt(update));
            }
            else if ("?????????? ????????????????????".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.findProvider(update));
            }
            else if ("???????????????????????? ????????????????".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.surveer(update));
            }
            else if ("?????????????????????????? ????????????????".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.respKeeping(update));
            }
            else if ("?????????????? ??????????????????????".equalsIgnoreCase(command)){
                executeMessage(setsHandler.opt(update));
            }
            else if ("?????????????????????? ????????????????".equalsIgnoreCase(command)){
                executeMessage(setsHandler.complex(update));
            }
            else if ("???????????????????? ????????????????????".equalsIgnoreCase(command)){
                executeMessage(setsHandler.customs(update));
            }
            else if ("?????????????????????? ????????????????????".equalsIgnoreCase(command)){
                executeMessage(setsHandler.provider(update));
            }
            else if ("????????????????".equalsIgnoreCase(command)){
                executeMessage(orderHandler.order(update));
            }
            else if ("??????????-????????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.startAdmin(update));
            }
            else if ("??????????/?????????? ???? ????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.online(update));
            }
            else if ("?????????? ???? ??????????-????????????".equalsIgnoreCase(command)){
                executeMessage(mainHandler.start(update));
            }
            else if ("?????????????????????????? ????????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.whoIsOnline(update));
            }
            else if ("???????????? ??????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.issuePermissions(update));
            }
            else if ("???????????? ??????????".equalsIgnoreCase(command)){
                executeMessage(manager.listOfQuestions(update));
            }
            else if ("???????????? ?????????? ??????????????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.listOfManagers(update));
            }
            else if ("?? ????????????????????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.aboutUser(update));
            }
            else if ("???????????? ??????????????????".equalsIgnoreCase(command)){
                adminPanel.dialogueOfManager(update).forEach(this::executeMessage);
            }
            else if ("??????????????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.analytics(update));
            }
            else if ("??????-???? ????????????????".equalsIgnoreCase(command)){
                executeMessage(adminPanel.suggestDate(update));
            }

        }
        else if (update.hasCallbackQuery()){
            if ("chat_with_manager".equalsIgnoreCase(update.getCallbackQuery().getData())){
                 executeMessage(mainHandler.startOnlineChat(update));
            }
        }
    }

    public void executeMessage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        }catch (TelegramApiException telegramApiException){
            log.error("???????????? ?????? ???????????????? ?????????????????? ???????????????????????? {}", sendMessage.getChatId());
        }
    }

    public SendMessage back(Update update){
        UserEntity userEntity = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String position = userEntity.getPosition();
        userEntity.setPosition("start");
        userService.update(userEntity);

        if (position.split("_").length > 1 && "admin".equalsIgnoreCase(position.split("_")[0])){
            return adminPanel.startAdmin(update);
        }
        else {
            return mainHandler.start(update);
        }
    }
}
