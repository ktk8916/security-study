package com.study.gitoauth.oauth2;

import com.study.gitoauth.auth.PrincipalDetails;
import com.study.gitoauth.domain.entity.User;
import com.study.gitoauth.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // oauth 해주는 곳
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 거기서 사용하는 고유 id github는 id라고 쓰고, google은 sub임 나중에 여러개 쓰면 잘 구분
        System.out.println(oAuth2User.getAttribute("id").getClass());
        Long providerId = Long.valueOf(oAuth2User.getAttribute("id").toString());

        // github 의 경우에는 Attributes에 email이 들어있지 않을수도 있음
        String email = oAuth2User.getAttribute("email");

        String username = provider + "_" + providerId;

        // 이 유저는 oauth를 사용해서 로그인을 할 것이기 때문에 password가 큰 의미가 없음
        // 걍 대충 박아놓자
        // 어차피 이거로 폼로그인 하려고 해도 복호화 과정때문에 안됨
        String password = UUID.randomUUID().toString();

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            User joinUser = User.builder()
                    .providerId(providerId)
                    .provider(provider)
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(User.Role.USER)
                    .build();

            return(new PrincipalDetails(userRepository.save(joinUser), oAuth2User.getAttributes()));
        } else {
            return new PrincipalDetails(user.get(), oAuth2User.getAttributes());
        }
    }
}
