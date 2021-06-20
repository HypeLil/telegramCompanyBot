package com.nikita.telegramBot.bot;

import com.nikita.telegramBot.bot.handler.OrderHandler;
import com.nikita.telegramBot.bot.handler.SetsHandler;
import com.nikita.telegramBot.bot.services.Sertification;
import com.nikita.telegramBot.bot.services.ServiceDT;
import com.nikita.telegramBot.bot.services.cargos.CargoChecker;
import com.nikita.telegramBot.bot.services.cargos.China;
import com.nikita.telegramBot.bot.services.cargos.Turkish;
import com.nikita.telegramBot.bot.handler.MainHandler;
import com.nikita.telegramBot.bot.handler.ServiceCatalog;
import com.nikita.telegramBot.model.User;
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

    @Value("${bot.admin}")
    private String botAdmin;

    private final MainHandler mainHandler;
    private final UserService userService;
    private final ServiceCatalog serviceCatalog;
    private final CargoChecker cargoChecker;
    private final Turkish turkish;
    private final China china;
    private final ServiceDT serviceDT;
    private final Sertification sertification;
    private final SetsHandler setsHandler;
    private final OrderHandler orderHandler;

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
        User user = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        if (update.hasMessage()) {
            String command = update.getMessage().getText();

            if ("Назад".equalsIgnoreCase(command)){
                executeMessage(back(update));
                return;
            }

            // проверка на наличие ввода данных для заявки на заказ
            if (!user.getPosition().equalsIgnoreCase("start")){
                String position = user.getPosition();
                if ("enter_name".equalsIgnoreCase(position)){
                    executeMessage(orderHandler.enterName(update));
                }
                else if ("enter_number".equalsIgnoreCase(position)){
                    executeMessage(orderHandler.enterNumber(update));
                }
                else if ("enter_email".equalsIgnoreCase(position)){
                    executeMessage(orderHandler.result(update));
                }
            }

            if ("carg".equalsIgnoreCase(user.getPosition().split("_")[0]) && !"Назад".equalsIgnoreCase(command)){
                executeMessage(cargoChecker.cargo(update));
            }
                userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
            if ("/start".equalsIgnoreCase(command)) {
                executeMessage(mainHandler.start(update));
            }
            else if ("Информация о компании".equalsIgnoreCase(command)){
                executeMessage(mainHandler.aboutCompany(update));
            }
            else if ("Онлайн-чат".equalsIgnoreCase(command)){
                executeMessage(mainHandler.chat(update));
            }
            else if ("Позвонить".equalsIgnoreCase(command)){
                executeMessage(mainHandler.call(update));
            }
            else if ("Готовые наборы".equalsIgnoreCase(command)){
                executeMessage(mainHandler.completedSets(update));
            }
            else if ("Каталог услуг".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.startService(update));
            }
            else if ("Грузоперевозки".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.cargo(update));
            }
            else if ("Подача ДТ".equalsIgnoreCase(command)){
                executeMessage(serviceCatalog.serviceDt(update));
            }
            else if (command.split(" ")[0].equalsIgnoreCase("Грузоперевозка")){
                executeMessage(cargoChecker.checker(update));
            }
            else if ("Авиадоставка грузов из Турции".equalsIgnoreCase(command)){
                executeMessage(turkish.sky(update));
            }
            else if ("Автомобильные перевозки грузов из Турциии".equalsIgnoreCase(command)){
                executeMessage(turkish.auto(update));
            }
            else if ("Мультимодальные перевозки грузов из Турции".equalsIgnoreCase(command)){
                executeMessage(turkish.multi(update));
            }
            else if ("Автоперевозки из Китая".equalsIgnoreCase(command)){
                executeMessage(china.auto(update));
            }
            else if ("Железнодорожные перевозки из Китая".equalsIgnoreCase(command)){
                executeMessage(china.train(update));
            }
            else if ("Авиадоставка из Китая".equalsIgnoreCase(command)){
                executeMessage(china.sky(update));
            }
            else if ("Подача ДТ под печатью брокера".equalsIgnoreCase(command)){
                executeMessage(serviceDT.broker(update));
            } else if ("Подача ДТ под печатью клиента".equalsIgnoreCase(command)){
                executeMessage(serviceDT.client(update));
            }
            else if ("Сертификация".equalsIgnoreCase(command)){
                executeMessage(sertification.start(update));
            }
            else if ("Таможенное оформление".equalsIgnoreCase(command) && "catalog".equalsIgnoreCase(user.getPosition())){
                executeMessage(sertification.customs(update));
            }
            else if ("Поиск поставщика".equalsIgnoreCase(command)){
                executeMessage(sertification.findProvider(update));
            }
            else if ("Оптовая дистрибуция".equalsIgnoreCase(command)){
                executeMessage(setsHandler.opt(update));
            }
            else if ("Комплексная доставка".equalsIgnoreCase(command)){
                executeMessage(setsHandler.complex(update));
            }
            else if ("Таможенное оформление".equalsIgnoreCase(command)){
                executeMessage(setsHandler.customs(update));
            }
            else if ("Проверенные поставщики".equalsIgnoreCase(command)){
                executeMessage(setsHandler.provider(update));
            }
            else if ("Заказать".equalsIgnoreCase(command)){
                executeMessage(orderHandler.order(update));
            }

        }
    }

    public void executeMessage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        }catch (TelegramApiException telegramApiException){
            log.error("Ошибка при отправке сообщения пользователю {}", sendMessage.getChatId());
        }
    }

    public SendMessage back(Update update){
        User user = userService.getOrCreate(String.valueOf(update.getMessage().getFrom().getId()));
        String position = user.getPosition();
        user.setPosition("start");
        userService.update(user);

        if ("carg_europe".equalsIgnoreCase(position)){
            return serviceCatalog.startService(update);
        }
        else if ("turkish".equalsIgnoreCase(position.split("_")[0])){
            return serviceCatalog.startService(update);
        }
        else if ("china".equalsIgnoreCase(position.split("_")[0])){
            return serviceCatalog.startService(update);
        }
        else if ("dt-service".equalsIgnoreCase(position.split("_")[0])){
            return serviceCatalog.startService(update);
        }
        else if ("sertif".equalsIgnoreCase(position.split("_")[0])){
            return serviceCatalog.startService(update);
        }
        else if ("sets".equalsIgnoreCase(position)){
            return mainHandler.completedSets(update);
        }
        else {
            return mainHandler.start(update);
        }
    }
}
