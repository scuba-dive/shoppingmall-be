package io.groom.scubadive.shoppingmall.member.controller;

import io.groom.scubadive.shoppingmall.global.dto.ApiResponseDto;
import io.groom.scubadive.shoppingmall.member.dto.response.UserAdminResponse;
import io.groom.scubadive.shoppingmall.member.dto.response.UserAdminStatusUpdateResponse;
import io.groom.scubadive.shoppingmall.member.service.UserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class UserAdminController {

    private final UserAdminService userAdminService;

    @Operation(summary = "전체 사용자 목록 조회", description = "관리자가 모든 사용자 정보를 페이징 형식으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 목록 조회에 성공하였습니다."),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponseDto<Page<UserAdminResponse>> getAllUsers(
            @Parameter(description = "페이지 정보 (page, size, sort)", example = "?page=0&size=10")
            Pageable pageable
    ) {
        Page<UserAdminResponse> users = userAdminService.getAllUsers(pageable);
        return ApiResponseDto.of(200, "사용자 목록 조회에 성공하였습니다.", users);
    }

    @Operation(summary = "사용자 상태 변경", description = "관리자가 특정 사용자의 상태를 활성/비활성으로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<UserAdminStatusUpdateResponse>> updateUserStatus(
            @Parameter(description = "상태를 변경할 사용자 ID", example = "1")
            @PathVariable Long id
    ) {
        UserAdminStatusUpdateResponse response = userAdminService.updateStatus(userId);
        return ResponseEntity.ok(
                ApiResponseDto.of(200, "사용자 상태 변경 성공", response)
        );
    }
}
