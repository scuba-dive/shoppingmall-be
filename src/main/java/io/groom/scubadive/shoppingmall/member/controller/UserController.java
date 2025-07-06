package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.global.securirty.LoginUser;
import io.groom.scubadive.shoppingmall.global.util.CookieUtil;
import io.groom.scubadive.shoppingmall.member.dto.request.SignInRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.SignUpRequest;
import io.groom.scubadive.shoppingmall.member.dto.request.UpdateUserRequest;
import io.groom.scubadive.shoppingmall.member.dto.response.*;
import io.groom.scubadive.shoppingmall.member.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {
                "http://localhost:5173",
        },
        allowCredentials = "true"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Tag(name = "Public API", description = "비회원 공개 API")
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 오류", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.status(201)
                .body(ApiResponseDto.of(201, "회원가입이 완료되었습니다.", response));
    }

    @Tag(name = "Public API", description = "비회원 공개 API")
    @Operation(summary = "이메일 인증", description = "회원가입시 이메일 중복 체크를 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 중복 체크 성공"),
            @ApiResponse(responseCode = "401", description = "이메일 중복 체크 실패", content = @Content)
    })
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<Void>> checkEmailDuplicate(@RequestParam String email) {
        userService.validateEmailDuplication(email);
        return ResponseEntity.ok(ApiResponseDto.of(200, "사용 가능한 이메일입니다.", null));
    }


    @Tag(name = "Public API", description = "비회원 공개 API")
    @Operation(summary = "로그인", description = "사용자가 로그인하고 AccessToken과 RefreshToken을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<SignInResponse>> login(
            @Valid @RequestBody SignInRequest request,
            HttpServletResponse response
    ) {
        LoginResult loginResult = userService.login(request);

        SignInResponse signInResponse = new SignInResponse(
                loginResult.getAccessToken(),
                loginResult.getUser()
        );

        // Refresh Token을 HttpOnly 쿠키로 저장
        CookieUtil.addRefreshTokenCookie(response, loginResult.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponseDto.of(200, "로그인에 성공하였습니다.", signInResponse)
        );
    }

    @Tag(name = "User API", description = "회원 전용 API")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserInfoResponse>> getMyInfo(@LoginUser Long userId) {
        UserInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(
                ApiResponseDto.of(200, "내 정보 조회에 성공하였습니다.", response)
        );
    }

    @Tag(name = "User API", description = "회원 전용 API")
    @Operation(summary = "로그아웃", description = "로그인한 사용자를 로그아웃 처리하고 RefreshToken 쿠키를 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(
            @LoginUser Long userId,
            HttpServletResponse response
    ) {
        userService.logout(userId, response);
        return ResponseEntity.ok(
                ApiResponseDto.of(200, "로그아웃에 성공하였습니다.", null)
        );
    }

    @Tag(name = "프론트 미구현 API", description = "회원 전용 API")
    @Operation(summary = "내 정보 수정", description = "로그인한 사용자의 비밀번호, 닉네임, 전화번호를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponseDto<UpdateUserResponseWrapper>> updateMyInfo(
            @LoginUser Long userId,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UpdateUserResponseWrapper response = userService.updateMyInfo(userId, request);
        return ResponseEntity.ok(
                ApiResponseDto.of(200, "내 정보가 성공적으로 수정되었습니다.", response)
        );
    }
}

