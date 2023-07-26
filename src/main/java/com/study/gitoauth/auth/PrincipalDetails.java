package com.study.gitoauth.auth;

import com.study.gitoauth.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

// 시큐리티가 로그인 요청을 완료하면
// 시큐리티 세션을 만들어줌
// 이 때 세션에 들어가는 객체는 Authentication 타입이고,
// Authentication 안에는 User의 정보가 담겨야 한다.
// 이게 UserDetails 타입 객체
// 그래서 UserDetails를 구현한 PrincipalDetails를 만들어 사용
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
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
}
