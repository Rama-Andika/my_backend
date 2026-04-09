package com.oxysystem.general.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String userId;
    private String fullName;
    private String userGroup;
    private Integer level;
    private String token;
    private String refreshToken;
}
