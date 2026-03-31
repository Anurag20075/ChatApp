package com.chat_app.service;

import java.util.List;

import com.chat_app.exception.ChatException;
import com.chat_app.exception.UserException;
import com.chat_app.model.Chat;
import com.chat_app.model.User;
import com.chat_app.request.GroupChatRequest;

public interface ChatService {
    public Chat createChat(User reqUser, Integer userId2) throws UserException;

    public Chat findChatById(Integer chatId) throws ChatException;

    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;

    public Chat createGroupChat(GroupChatRequest req, User reqUser) throws UserException;

    public Chat addUserToGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException;

    public Chat removeUserFromGroup(Integer chatId, Integer userId, User reqUser)
            throws UserException, ChatException;

    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException;

    public Chat deleteChat(Integer chatId, User reqUser) throws ChatException, UserException;
}
