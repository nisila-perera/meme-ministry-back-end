package com.webdesk.service.impl;

import com.webdesk.dto.UserPrincipal;
import com.webdesk.entity.User;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.AuthDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsServiceImpl implements AuthDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserPrincipal(user);
    }
}
