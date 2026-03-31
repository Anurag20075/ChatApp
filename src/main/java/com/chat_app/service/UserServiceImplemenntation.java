package com.chat_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.chat_app.AppConfig.TokenProvider;
import com.chat_app.exception.UserException;
import com.chat_app.model.User;
import com.chat_app.repositary.UserRepositary;
import com.chat_app.request.UpdateuserRequest;

@Service
public class UserServiceImplemenntation implements UserService {
    private UserRepositary userRepo;
    private TokenProvider tokenProvider;

    public UserServiceImplemenntation(UserRepositary userRepo, TokenProvider tokenProvider) {
        this.userRepo = userRepo;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UserException("User not found with id: " + id);

    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if (email == null) {
            throw new BadCredentialsException("Invalid token: email not found");
        }
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found" + email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateuserRequest req) throws UserException {
        User user = findUserById(userId);
        if (req.getFull_name() != null) {
            user.setFull_name(req.getFull_name());
        }
        if (req.getProfile_pic() != null) {
            user.setProfile_pic(req.getProfile_pic());
        }
        return userRepo.save(user);
    }

    @Override
    public List<User> searchUsers(String query) {
        List<User> users = userRepo.searchUser(query);
        return users;
    }

}
