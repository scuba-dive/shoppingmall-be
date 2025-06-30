package io.groom.scubadive.shoppingmall.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserResponseWrapper {
    private UserInfoResponse user;
    private boolean loggedOut;
}
