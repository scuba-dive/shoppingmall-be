package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.dto.response.UserAdminResponse;
import io.groom.scubadive.shoppingmall.member.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    public ApiResponseDto<Page<UserAdminResponse>> getAllUsers(Pageable pageable) {
        Page<UserAdminResponse> users = userAdminService.getAllUsers(pageable);
        return ApiResponseDto.of(200, "사용자 목록 조회에 성공하였습니다.", users);
    }

}
