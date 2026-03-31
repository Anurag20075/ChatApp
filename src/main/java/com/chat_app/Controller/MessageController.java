package com.chat_app.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ChatException;
import com.chat_app.exception.UserException;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.request.SendMessageRequest;
import com.chat_app.response.ApiResponse;
import com.chat_app.service.MessageService;
import com.chat_app.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;
    private UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        req.setUserId(user.getId());
        Message message = messageService.sendMessage(req);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        List<Message> messages = messageService.getMessage(chatId, user);
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        Message message = messageService.findMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessage(@PathVariable Integer messageId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);
        ApiResponse res = new ApiResponse("Message deleted successfully", true);
        return ResponseEntity.ok(res);
    }
}
