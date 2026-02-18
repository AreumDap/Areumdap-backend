package com.umc9th.areumdap.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record SignUpRequest(

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 30, message = "이름은 30자를 초과할 수 없습니다.")
        String name,

        @NotNull(message = "생년월일은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birth,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상 입력해야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-])[A-Za-z\\d!@#$%^&*()_+=\\-]{8,}$",
                message = "비밀번호는 8자 이상이며 영문, 숫자, 특수문자를 모두 포함해야 합니다."
        )
        String password
) {
        public static SignUpRequest from(String name, LocalDate birth, String email, String password) {
                return new SignUpRequest(name, birth, email, password);
        }
}
