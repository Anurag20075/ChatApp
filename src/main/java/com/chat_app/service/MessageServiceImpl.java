package com.chat_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.chat_app.exception.ChatException;
import com.chat_app.exception.UserException;
import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.repositary.MessageRepositary;
import com.chat_app.request.SendMessageRequest;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepositary messageRepository;
    private ChatService chatService;
    private UserService userService;

    public MessageServiceImpl(MessageRepositary messageRepository, ChatService chatService,
            UserService userService) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.userService = userService;
    }

    @Override
    public Message sendMessage(SendMessageRequest req) throws ChatException, UserException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        return message;
    }

    @Override
    public List<Message> getMessage(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(reqUser)) {
            throw new UserException("You are not part of this chat");
        }
        List<Message> messages = messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws ChatException, UserException {

        Optional<Message> opt = messageRepository.findById(messageId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new ChatException("Message not found");
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws ChatException, UserException {
        Message message = messageRepository.findById(messageId).get();
        if (!message.getUser().getId().equals(reqUser.getId())) {
            messageRepository.deleteById(messageId);
        }
        throw new UserException("You are not authorized to delete this message");
    }

}
