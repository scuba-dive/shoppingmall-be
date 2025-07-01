package io.groom.scubadive.shoppingmall.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 30, message = "비밀번호는 4자 이상 30자 이하여야 합니다.")
    @Schema(description = "비밀번호", example = "securePassword123")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    @Schema(description = "비밀번호 확인", example = "securePassword123")
    private String passwordCheck;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 30, message = "이름은 2자 이상 30자 이하여야 합니다.")
    @Schema(description = "사용자 실명", example = "홍길동")
    private String username;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNumber;

    @NotBlank(message = "주소는 필수입니다.")
    @Schema(description = "주소", example = "서울특별시 강남구 역삼동 123-45")
    private String address;
}
