package com.chat_app.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import com.chat_app.AppConfig.TokenProvider;
import com.chat_app.exception.UserException;
import com.chat_app.model.User;
import com.chat_app.repositary.UserRepositary;
import com.chat_app.request.LoginRequest;
import com.chat_app.response.AuthResponse;
import com.chat_app.service.CustomUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepositary userRepositary;
    private final PasswordEncoder passwordEncoder;
    private  TokenProvider tokenProvider;
    private final CustomUserService  customUserService;

    public AuthController(UserRepositary userRepositary, PasswordEncoder passwordEncoder,CustomUserService  customUserService) {
        this.userRepositary = userRepositary;
        this.passwordEncoder = passwordEncoder;
        this.customUserService=customUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        
        String email = user.getEmail();
        String full_name = user.getFull_name();
        String password = user.getPassword();

        User isUser = userRepositary.findByEmail(email);
        if (isUser != null) {
            throw new UserException("This email is being used in another account: " + email);
        }

        // Create and save new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFull_name(full_name);
        createdUser.setPassword(passwordEncoder.encode(password));
        userRepositary.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt, true);
        
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws UserException {
        
        String email = req.getEmail();
        String password = req.getPassword();
        Authentication  authentication=authentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        
        return new ResponseEntity<AuthResponse>(res,HttpStatus.OK);
    }
    public Authentication authentication(String Username, String password){
        UserDetails userDetails=customUserService.loadUserByUsername(Username);

        if (userDetails!=null) {
            throw new BadCredentialsException("Invalid Username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password or Username");
        }
         return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    }
}