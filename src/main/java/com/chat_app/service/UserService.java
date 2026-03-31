package com.chat_app.service;

import java.util.List;

import com.chat_app.exception.UserException;
import com.chat_app.model.User;
import com.chat_app.request.UpdateuserRequest;

public interface UserService {

        public User findUserById(Integer id) throws UserException;

        public User findUserProfile(String jwt) throws UserException;

        public User updateUser(Integer userId, UpdateuserRequest req) throws UserException;

        public List<User> searchUsers(String query);

}
