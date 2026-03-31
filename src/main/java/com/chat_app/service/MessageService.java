package com.chat_app.service;

import java.util.List;

import com.chat_app.exception.ChatException;
import com.chat_app.exception.UserException;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.request.SendMessageRequest;

public interface MessageService {

    public Message sendMessage(SendMessageRequest req) throws ChatException, UserException;

    public List<Message> getMessage(Integer chatId, User reqUser) throws ChatException, UserException;

    public Message findMessageById(Integer messageId) throws ChatException, UserException;

    public void deleteMessage(Integer messageId, User reqUser) throws ChatException, UserException;
}
