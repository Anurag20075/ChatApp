package com.chat_app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Chat {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String chat_name;
  private String chat_image;

  @ManyToMany
  private Set<User> admin = new HashSet<>();

  public Chat(Integer id, String chat_name, String chat_image, Set<User> admin, boolean isGroup, User createdBy,
      Set<User> users, List<Message> messages) {
    this.id = id;
    this.chat_name = chat_name;
    this.chat_image = chat_image;
    this.admin = admin;
    this.isGroup = isGroup;
    this.createdBy = createdBy;
    this.users = users;
    this.messages = messages;
  }

  @Column(name = "is_group")
  private boolean isGroup;

  @JoinColumn(name = "created_by")
  @ManyToOne
  private User createdBy;

  @ManyToMany
  private Set<User> users = new HashSet<>();

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  public Chat() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getChat_name() {
    return chat_name;
  }

  public void setChat_name(String chat_name) {
    this.chat_name = chat_name;
  }

  public String getChat_image() {
    return chat_image;
  }

  public void setChat_image(String chat_image) {
    this.chat_image = chat_image;
  }

  public boolean isGroup() {
    return isGroup;
  }

  public void setGroup(boolean group) {
    isGroup = group;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public Set<User> getAdmin() {
    return admin;
  }

  public void setAdmin(Set<User> admin) {
    this.admin = admin;
  }

}
