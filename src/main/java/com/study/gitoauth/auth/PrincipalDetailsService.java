package com.study.gitoauth.auth;

import com.study.gitoauth.domain.entity.User;
import com.study.gitoauth.exception.UserNotFoundException;
import com.study.gitoauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl로 로그인 요청이 오면
// bean에 등록된 UserDetailsService의 loadUserByUsername를 실행
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return new PrincipalDetails(user);
    }
}
