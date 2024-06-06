package kr.co.sboard.security;

import kr.co.sboard.entity.User;
import kr.co.sboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findById(username);

        //log.info("here1 : " + result);
        UserDetails userDetails = null;

        if(!result.isEmpty()){
            //log.info("here2");
            // 해당하는 사용자가 존재하면 인증 객체 생성
            User user = result.get();

            //log.info("here3 : " + user.toString());

            userDetails = MyUserDetails.builder().user(user).build();
        }

        log.info("here4 : " + userDetails);
        // Security ContextHolder 저장
        return userDetails;
    }
}