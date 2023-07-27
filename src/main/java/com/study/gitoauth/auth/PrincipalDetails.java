package com.study.gitoauth.auth;

import com.study.gitoauth.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

// 시큐리티가 로그인 요청을 완료하면
// 시큐리티 세션을 만들어줌
// 이 때 세션에 들어가는 객체는 Authentication 타입
// 그래서 UserDetails를 구현한 PrincipalDetails를 만들어 사용

// 일반 로그인과 oauth2 로그인은 서로 타입이 달라서 세션에서 꺼내올 때 구분해야 함
// 이를 구분하지 않기 위해 PrincipalDetails에 두 객체를 모두 구현해서 타입을 맞춰줌
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;
    
    // 일반 로그인을 할 때 사용되는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // oauth 로그인을 할 때 사용되는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    // 해당 유저의 권한을 반환해야함
    // 권한은 Collection<GrantedAuthority> 타입으로 만들어 반환해야 함
    //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        collection.add((GrantedAuthority) () -> user.getRole().toString());
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 이건 OAuth2User 에서의 name임
    // oauth provider의 고유 id를 반환해주자
    // 잘 안쓴다곤 함....
    @Override
    public String getName() {
        return (String) attributes.get("id");
    }
}
