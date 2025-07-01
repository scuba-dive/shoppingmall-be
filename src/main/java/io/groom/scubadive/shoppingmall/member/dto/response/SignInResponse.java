package io.groom.scubadive.shoppingmall.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResponse {

    private String accessToken;
    private UserSummary user;

}
