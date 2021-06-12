package com.nikita.telegramBot.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "usr")
@NoArgsConstructor
public class User {

  @Id
  @Column(name = "usr_id")
  private String userId;
  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;
  @Column(name = "position")
  private String position;

  public User(String userId) {
    this.userId = userId;
    this.role = Role.USER;
    this.position = "start";
  }
}
