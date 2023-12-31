package com.example.team258.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserSignupRequestDto {


    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{4,10}", message = "아이디 4~10자 영문 소문자, 숫자를 사용하세요.")
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,15}+$", message = "비밀번호는 8~15자 영문 대 소문자, 숫자를 사용하세요.")
    private String password1;

    private String password2;

    @Builder.Default
    private boolean admin = false;
    @Builder.Default
    private String adminToken = "";


}
