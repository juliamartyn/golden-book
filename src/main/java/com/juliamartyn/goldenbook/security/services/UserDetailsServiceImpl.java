package com.juliamartyn.goldenbook.security.services;

import com.juliamartyn.goldenbook.entities.User;
import com.juliamartyn.goldenbook.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);

        if(user != null && user.getDisabled() != true) {
            return UserPrinciple.build(user);
        } else {
            throw new UsernameNotFoundException("User with username `" + username + "` Not Found or blocked");
        }

    }
}