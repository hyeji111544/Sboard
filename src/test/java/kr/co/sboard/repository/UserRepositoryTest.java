package kr.co.sboard.repository;

import groovy.util.logging.Slf4j;
import kr.co.sboard.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("아이디이메일로 찾기")
    public void findUserByUidAndEmail(){

        User user = User.builder()
                .name("김철수")
                .email("matal_158@naver.com").build();


    }
}