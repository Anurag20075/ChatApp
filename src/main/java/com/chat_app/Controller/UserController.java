package com.chat_app.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.UserException;
import com.chat_app.model.User;
import com.chat_app.request.UpdateuserRequest;
import com.chat_app.response.ApiResponse;
import com.chat_app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token)
            throws UserException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String q) {
        List<User> users = userService.searchUsers(q);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/updat")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateuserRequest req,
            @RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(), req);
        ApiResponse res = new ApiResponse("user updated succesfully", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
