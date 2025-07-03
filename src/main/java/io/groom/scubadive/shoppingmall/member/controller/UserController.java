package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.securirty.LoginUser;
import io.groom.scubadive.shoppingmall.global.util.CookieUtil;
import io.groom.scubadive.shoppingmall.member.dto.request.SignInRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.UpdateUserRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.UpdateUserResponseWrapper;
import io.groom.scubadive.shoppingmall.member.dto.response.UserInfoResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.SignInResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.UserResponse;
import io.groom.scubadive.shoppingmall.member.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<Void>> checkEmailDuplicate(@RequestParam String email) {
        userService.validateEmailDuplication(email);
        return ResponseEntity.ok(ApiResponseDto.of(200, "사용 가능한 이메일입니다.", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<SignInResponse>> login(
            @RequestBody @Valid SignInRequest request,
            HttpServletResponse response
    ) {
        SignInResponse signInResponse = userService.login(request);

        // Refresh Token을 HttpOnly 쿠키로 설정
        CookieUtil.addRefreshTokenCookie(response, signInResponse.getAccessToken());

        // 응답 데이터는 AccessToken + UserSummary
        return ResponseEntity.ok(
                ApiResponseDto.of(200, "로그인에 성공하였습니다.", signInResponse)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserInfoResponse>> getMyInfo(@LoginUser Long userId) {
        UserInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(ApiResponseDto.of(200, "내 정보 조회에 성공하였습니다.", response));
    }


    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto<UpdateUserResponseWrapper>> updateMyInfo(
            @LoginUser Long userId,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UpdateUserResponseWrapper response = userService.updateMyInfo(userId, request);
        return ResponseEntity.ok(ApiResponseDto.of(200, "내 정보가 성공적으로 수정되었습니다.", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(@LoginUser Long userId, HttpServletResponse response) {
        userService.logout(userId, response);
        return ResponseEntity.ok(ApiResponseDto.of(200, "로그아웃에 성공하였습니다.", null));
    }




}
