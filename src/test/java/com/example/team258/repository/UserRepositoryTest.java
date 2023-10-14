package com.example.team258.repository;

import com.example.team258.entity.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.example.team258.entity.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @DataJpaTest 어노테이션을 사용하여 JPA 관련 구성만 로드
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired TestEntityManager em;
    @Autowired UserRepository userRepository;
    @Test
    void findByUsername() {
        //given
        User user = User.builder()
                .username("bin0222")
                .password("123")
                .role(UserRoleEnum.USER)
                .build();

        em.persist(user);
        em.flush();

        //when
        Optional<User> foundUser = userRepository.findByUsername("bin0222");

        //then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
    }
}