package kr.co.sboard.controller;

import kr.co.sboard.config.AppInfo;
import kr.co.sboard.entity.User;
import kr.co.sboard.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {


    /*
        - SecurityContextHolder에 저장된 Authentication 사용자 인증객체에서 Principal 즉 MyUserDetails(User 엔티티)를 구해 View에 email 출력
        - 물론 Template View에서 직접 Thymeleaf sec속성을 이용해 Security 인증객체를 출력할 수 있지만 여기서는 직접 Security Authentication 객체에 접근
    */

    @GetMapping(value = {"/", "/index"})
    public String index(Authentication authentication, Model model){

        //SecurityContextHolder의 Authentication의 principal 가져오기
/*
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        log.info("user : " + user);
*/

        return "/index";
    }
}
