package com.example.team258.service;

import com.example.team258.domain.member.dto.UserSignupRequestDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.exception.DuplicateUsernameException;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.common.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * 사용자 서비스 관련 단위 테스트 클래스로 변환
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS) 어노테이션을 통해
 * 테스트 클래스의 인스턴스 수명 주기를 설정하는 데 사용
 * TestInstance.Lifecycle.PER_CLASS 설정을 사용하면 테스트 클래스의 단일 인스턴스만 생성
 * 이 인스턴스는 모든 테스트 메서드에 대해 재사용된다.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceUnitTest {
    /**
     * UserRepository에 대한 모의 객체 생성
     */
    @Mock
    private UserRepository userRepository;

    /**
     * UserService에 대한 모의 객체 생성
     */
    private UserService userService;

    /**
     * SecurityUtil의 static 메서드를 mock하기 위한 객체
     */
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    /**
     * PasswordEncoder 모의 객체 생성
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * SecurityUtil 모의 객체 생성
     */
    @Mock
    private SecurityUtil securityUtil;

    /**
     * 모든 테스트 시작하기 전에 실행하는 설정 메서드 이다.
     */
    @BeforeAll
    void setUp() {
        /**
         * Mockito 어노테이션을 활성화하여 Mock 객체 초기화
         */
        MockitoAnnotations.openMocks(this);

        /**
         * UserService 객체 생성
         */
        userService = new UserService(userRepository, passwordEncoder);

        /**
         * SecurityUtil의 static 메서드를 mock하기 위한 객체 생성
         */
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }

    /**
     * 모든 테스트가 끝난 후에 실행하는 메서드
     */
    @AfterAll
    void tearDown() {
        /**
         * SecurityUtil의 static 메서드를 mock하기 위한 객체 닫기
         */
        mockedSecurityUtil.close();
    }

    @Test
    void passwordCheck_비밀번호_일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();

        // when
        // then
        assertDoesNotThrow(() -> userService.passwordCheck(requestDto));
    }

    @Test
    void passwordCheck_비밀번호_불일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .build();

        // when
        // then
        assertThrows(IllegalArgumentException.class,() -> userService.passwordCheck(requestDto));
    }

    @Test
    void userNameCheck_중복_아닐경우() {
        // given
        String username = "bin7777";
        String username2 = "bin7778";
        User user = User.builder()
                .username(username)
                .build();

        //when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //then
        assertDoesNotThrow(() -> userService.userNameCheck(username2));
    }
    @Test
    void userNameCheck_중복_일_경우() {
        // given
        String username = "bin7777";
        String username2 = "bin7777";
        User user = User.builder()
                .username(username)
                .build();

        //when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //then
        assertThrows(DuplicateUsernameException.class,() -> userService.userNameCheck(username2));

    }

    @Test
    void getUserRoleEnum_사용자() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .build();

        assertThat(userService.getUserRoleEnum(requestDto)).isEqualTo(UserRoleEnum.USER);

    }
    @Test
    void getUserRoleEnum_관리자_토큰_일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .admin(true)
                .adminToken("adminadminadminadminadmin")
                .build();

        assertThat(userService.getUserRoleEnum(requestDto)).isEqualTo(UserRoleEnum.ADMIN);

    }

    @Test
    void getUserRoleEnum_관리자_토큰_불일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .admin(true)
                .adminToken("adminadminadminadminadmn")
                .build();

        assertThrows(IllegalArgumentException.class,()->userService.getUserRoleEnum(requestDto));

    }
    @Test
    void signup() {
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();
        User user = User.builder()
                .build();

        // when
        when(userRepository.save(user)).thenReturn(user);
        MessageDto msg = userService.signup(requestDto).getBody();
        // then
        assertThat(msg.getMsg()).isEqualTo("회원가입이 완료되었습니다");

    }

    @Test
    void escape() {
        // given
        /**
         * HttpServletRequest, HttpServletResponse 모의 객체 생성
         */
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        /**
         * 현재 쓰레드 요청 속성에 모의 요청 및 응답 설정
         */
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();
        User user = User.builder()
                .username("bin0222")
                .build();

        // when
        /**
         * 모의 객체 동작 설정 : SecurityUtil.getPrincipal() 메서드가 Optional.ofNullable(user)를 반환하도록 설정
         */
        when(securityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        /**
         * 모의 객체 동작 설정 : userRepository.delete(user) 메서드가 아무것도 반환하지 않도록 설정
         */
        doNothing().when(userRepository).delete(user);

        MessageDto msg = userService.escape().getBody();
        // then
        assertThat(msg.getMsg()).isEqualTo("회원탈퇴가 완료되었습니다");
    }
}