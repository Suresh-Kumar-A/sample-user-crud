package com.gmail.creativegeeksuresh.crud.user.security;

import com.gmail.creativegeeksuresh.crud.user.model.User;
import com.gmail.creativegeeksuresh.crud.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User loggedInUser = userService.findByUsername(username);
            return new CustomUserDetails(loggedInUser);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            throw new UsernameNotFoundException("Invalid Credentials");
        }
    }

}