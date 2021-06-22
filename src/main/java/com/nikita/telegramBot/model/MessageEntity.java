package com.nikita.telegramBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "message")
@RequiredArgsConstructor
public class MessageEntity {

    @Id
    private int messageId;
    private String chatId;
    private String text;
    private String messageTime;
}
