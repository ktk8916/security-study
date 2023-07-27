package com.study.gitoauth.controller;

import com.study.gitoauth.auth.PrincipalDetails;
import com.study.gitoauth.domain.request.JoinRequest;
import com.study.gitoauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    // 밑에 두 메서드는 스프링 시큐리티 세션에 저장된 인증정보를 가져와줌
    // 스프링 시큐리티 세션안에 Authentication은 userDetails, OAuth2User 타입이 들어갈 수 있는데
    // 원래는 이를 폼 로그인에서 사용하는 userDetails 이랑 oauth에서 사용하는 OAuth2User로 따로 캐스팅해야함
    // 근데 이러면 번거로우니 PrincipalDetails 객체를 userDetails, OAuth2User를 모두 구현하는 객체로 만들어
    // 모든 로그인에 대응할 수 있도록 만듬

    // Authentication은 시큐리티 세션에 저장된 Authentication 객체를 가져와 줌
    // 이 안에는 PrincipalDetails를 얻을 수 있는 getPrincipal 메서드가 있고
    // 반환값이 Object이므로 형변환을 해줘야 함
    // PrincipalDetails은 User를 포함하고 있으므로 이 객체로 User 정보를 얻어올 수 있음
    @GetMapping("/test/form/login")
    public @ResponseBody String testFormLogin(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
        System.out.println("getUser = " + principal.getUser());
        System.out.println("getAttributes = " + principal.getAttributes());
        return "form 유저 정보 확인1";
    }
    
    // 위와 비슷하나 이번에는 OAuth2User로 형변환 하였음
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("getUser = " + oAuth2User.getAttributes());
        return "oauth 유저 정보 확인";
    }

    // @AuthenticationPrincipal 은 Authentication 안에 있는 PrincipalDetails 를 꺼내줌
    // authentication.getPrincipal() 라고 생각하면 됨
    // 다시 정리하자면.. 이젠 PrincipalDetails 객체가 userDetails, OAuth2User 타입을 모두 가지고 있어서
    // 두 로그인 방식에 모두 사용 가능
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("getUser = " + principalDetails.getUser());
        System.out.println("getAttributes = " + principalDetails.getAttributes());
        return "유저 정보 확인 통합";
    }


    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody Object user(@AuthenticationPrincipal PrincipalDetails principalDetails){

        return Arrays.asList(principalDetails.getUser(), principalDetails.getAttributes());
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(JoinRequest joinRequest){
        userService.join(joinRequest);
        return "redirect:/loginForm";
    }
}

