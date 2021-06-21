package com.nikita.telegramBot.model;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "usr")
@NoArgsConstructor
public class User {

  @Id
  @Column(name = "usr_id")
  private String userId;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String position;
  private String number;
  private String name;
  private String email;
  private String orderPosition;
  private boolean online;
  private String lastAction;

  public User(String userId) {
    this.userId = userId;
    this.role = Role.USER;
    this.position = "start";
    this.number = "0";
    this.name = "Без имени";
    this.email = "@";
    this.orderPosition = "";
    this.online = false;
    this.lastAction = new SimpleDateFormat("yyyy.MM.dd G 'в' HH:mm:ss").format(new Date());
  }
}
