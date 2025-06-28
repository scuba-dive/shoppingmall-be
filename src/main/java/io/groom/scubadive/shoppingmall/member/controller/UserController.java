package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.UserResponse;
import io.groom.scubadive.shoppingmall.member.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "회원가입이 완료되었습니다.", response));
    }
}
