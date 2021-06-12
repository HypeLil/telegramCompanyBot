package com.nikita.telegramBot.bot.services.cargos;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Cargo {

    public SendMessage start(Update update);
}
