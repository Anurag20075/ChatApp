package com.chat_app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.chat_app.exception.ChatException;
import com.chat_app.exception.UserException;
import com.chat_app.model.Chat;
import com.chat_app.model.User;
import com.chat_app.repositary.ChatRepo;
import com.chat_app.repositary.UserRepositary;
import com.chat_app.request.GroupChatRequest;

@Service
public class ChatServiceImplementation implements ChatService {

    private final ChatRepo chatRepo;
    private final UserRepositary userRepo;
    private final UserService userService;

    public ChatServiceImplementation(ChatRepo chatRepo, UserRepositary userRepo, UserService userService) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Override
    public Chat createChat(User reqUser, Integer userId2) throws UserException {
        User user = userService.findUserById(userId2);
        Chat isChatExist = chatRepo.findSingleChatUserByIds(reqUser, user);

        if (isChatExist != null)
            return isChatExist;

        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        return chatRepo.save(chat);
    }

    @Override
    public Chat createGroupChat(GroupChatRequest req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_name(req.getChatName());
        group.setChat_image(req.getChatImage());
        group.setCreatedBy(reqUser);

        group.getAdmin().add(reqUser);
        group.getUsers().add(reqUser); // Creator must be a member

        for (Integer userId : req.getUserIds()) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return chatRepo.save(group);
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        return chatRepo.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id: " + chatId));
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        return chatRepo.findChatByUserId(user.getId());
    }

    @Override
    public Chat addUserToGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Chat chat = findChatById(chatId);
        User user = userService.findUserById(userId);

        if (chat.getAdmin().contains(reqUser)) {
            chat.getUsers().add(user);
            return chatRepo.save(chat);
        }
        throw new UserException("Only Admin can add users");
    }

    @Override
    public Chat removeUserFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Chat chat = findChatById(chatId);
        User userToRemove = userService.findUserById(userId);

        if (chat.getAdmin().contains(reqUser) || userToRemove.getId().equals(reqUser.getId())) {
            chat.getUsers().remove(userToRemove);
            return chatRepo.save(chat);
        }
        throw new UserException("Not authorized to remove user");
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
        Chat chat = findChatById(chatId);
        if (chat.getAdmin().contains(reqUser)) {
            chat.setChat_name(groupName);
            return chatRepo.save(chat);
        }
        throw new UserException("Only Admin can rename group");
    }

    @Override
    public Chat deleteChat(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = findChatById(chatId);
        if (chat.getCreatedBy().getId().equals(reqUser.getId())) {
            chatRepo.delete(chat);
            return chat;
        }
        throw new UserException("Only the creator can delete this chat");
    }
}