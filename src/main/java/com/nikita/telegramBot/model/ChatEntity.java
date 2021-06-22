package com.nikita.telegramBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
@Getter
@Setter
@RequiredArgsConstructor
public class ChatEntity {

    @Id
    private String chatId;

    private String userId;
    private String managerId;
    // date
    private String lastMessage;
}
