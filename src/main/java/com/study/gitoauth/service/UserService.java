package com.study.gitoauth.service;

import com.study.gitoauth.domain.entity.User;
import com.study.gitoauth.domain.request.JoinRequest;
import com.study.gitoauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void join(JoinRequest joinRequest){
        User user = User.builder()
                .username(joinRequest.getUsername())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .email(joinRequest.getEmail())
                .role(User.Role.USER)
                .build();
        userRepository.save(user);
    }
}
