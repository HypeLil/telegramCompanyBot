package com.nikita.telegramBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "message")
@RequiredArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;
    private int chatId;
    private String text;
    private LocalDateTime messageTime;
    private String userId;
}
