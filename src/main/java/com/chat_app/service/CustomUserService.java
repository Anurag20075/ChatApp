package com.chat_app.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chat_app.model.User;
import com.chat_app.repositary.UserRepositary;

@Service
public class CustomUserService implements UserDetailsService {
    
    private UserRepositary userRepositary;
    public CustomUserService(UserRepositary userRepositary){
            this.userRepositary=userRepositary;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepositary.findByEmail(username);
        if (user== null) {
            throw new UsernameNotFoundException("User Not Found :"+username);
        }
        List<GrantedAuthority> authorities=new ArrayList<>();
       return new  org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }

}
