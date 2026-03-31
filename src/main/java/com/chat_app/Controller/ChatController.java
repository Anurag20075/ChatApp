package com.chat_app.Controller;

import com.chat_app.model.User;
import com.chat_app.model.Chat;
import com.chat_app.request.SingleChatRequest;
import com.chat_app.response.ApiResponse;
import com.chat_app.request.GroupChatRequest;
import com.chat_app.service.ChatService;
import com.chat_app.service.UserService;
import com.chat_app.exception.UserException; // Added for exception handling
import com.chat_app.exception.ChatException; // Added for exception handling

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(
            @RequestBody SingleChatRequest singleChatRequest,
            @RequestHeader("Authorization") String jwt) throws UserException {

        // 1. Fetch user from JWT
        User reqUser = userService.findUserProfile(jwt);

        // 2. Create the single chat
        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());

        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(
            @RequestBody GroupChatRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException {

        // 1. Fetch user from JWT
        User reqUser = userService.findUserProfile(jwt);

        // 2. Call createGroupChat (Matches your ServiceImplementation name)
        Chat chat = chatService.createGroupChat(req, reqUser);

        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        // 2. Call createGroupChat (Matches your ServiceImplementation name)
        Chat chat = chatService.findChatById(chatId);

        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(@RequestHeader("Authorization") String jwt)
            throws UserException {

        // 1. Fetch user from JWT
        User reqUser = userService.findUserProfile(jwt);

        // 2. Call createGroupChat (Matches your ServiceImplementation name)
        List<Chat> chat = chatService.findAllChatByUserId(reqUser.getId());

        return new ResponseEntity<List<Chat>>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(chatId, userId, reqUser);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        // 1. Fetch user from JWT
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeUserFromGroup(chatId, userId, reqUser);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        // 1. Get the full User object from the JWT
        User reqUser = userService.findUserProfile(jwt);

        // 2. Pass the User object to match your ServiceImplementation
        chatService.deleteChat(chatId, reqUser);

        // 3. Create a meaningful response
        ApiResponse res = new ApiResponse("Chat deleted successfully", true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}