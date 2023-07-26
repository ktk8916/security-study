package com.study.gitoauth.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class JoinRequest {

    private String username;
    private String email;
    private String password;

}
