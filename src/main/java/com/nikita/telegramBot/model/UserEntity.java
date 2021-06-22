package com.nikita.telegramBot.model;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {

  @Id
  private String userId;

  @Enumerated(EnumType.ORDINAL)
  private Role role;

  private String position;
  private String number;
  private String name;
  private String email;
  private String orderPosition;
  private boolean online;
  private LocalDateTime lastAction;

  public UserEntity(String userId) {
    this.userId = userId;
    this.role = Role.USER;
    this.position = "start";
    this.number = "0";
    this.name = "Без имени";
    this.email = "@";
    this.orderPosition = "";
    this.online = false;
    this.lastAction = LocalDateTime.now();
  }
}
