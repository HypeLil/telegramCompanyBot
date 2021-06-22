package com.nikita.telegramBot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TelegramUtil {

    public static String extractCommand(String text) {
        return text.split(" ")[0];
    }

    public static String extractArguments(String text) {
        return text.substring(text.indexOf(" ") + 1);
    }

}
