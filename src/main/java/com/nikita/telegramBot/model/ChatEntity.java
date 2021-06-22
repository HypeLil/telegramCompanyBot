package com.nikita.telegramBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat")
@Getter
@Setter
@RequiredArgsConstructor
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatId;

    private String userId;
    private String managerId;
    private Boolean answered;
    private LocalDateTime lastMessage;
}
