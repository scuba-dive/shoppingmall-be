package io.groom.scubadive.shoppingmall.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResult {

    private String accessToken;
    private String refreshToken;
    private UserSummary user;

}
