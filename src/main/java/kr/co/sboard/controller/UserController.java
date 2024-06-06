package kr.co.sboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.sboard.dto.TermsDTO;
import kr.co.sboard.dto.UserDTO;
import kr.co.sboard.config.AppInfo;
import kr.co.sboard.entity.User;
import kr.co.sboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final AppInfo appInfo;

    @GetMapping("/user/login")
    public String login(@ModelAttribute("success") String success, Model model){
        // 매개변수 success에 @ModelAttribute 선언으로 View 참조할 수 있음

        return "/user/login";
    }

    @GetMapping("/user/terms")
    public String terms(Model model){

        TermsDTO termsDTO = userService.selectTerms();
        model.addAttribute("terms", termsDTO);

        return "/user/terms";
    }

    @GetMapping("/user/register")
    public String register(@ModelAttribute("sms") String sms){
        return "/user/register";
    }

    @PostMapping("/user/register")
    public String register(HttpServletRequest req, UserDTO userDTO){

        String regip = req.getRemoteAddr();
        userDTO.setRegip(regip);

        log.info("insert....!! " + userDTO.toString());

        userService.insertUser(userDTO);

        return "redirect:/user/login?success=200";
    }

    @ResponseBody
    @GetMapping("/user/{type}/{value}")
    public ResponseEntity<?> checkUser(HttpSession session,
                                       @PathVariable("type")  String type,
                                       @PathVariable("value") String value){

        int count = userService.selectCountUser(type, value);
        log.info("count : " + count);
        log.info("type : " + type);


        // 중복 없으면 이메일 인증코드 발송
        if(count == 0 && type.equals("email")){
            log.info("email : " + value);
            userService.sendEmailCode(session, value);
        }

        // Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<?> sendEmailForFindUser(HttpSession session,
                                                  @PathVariable("email") String email){

        try {
            userService.sendEmailCode(session, email);
            // 이메일 성공적으로 보내짐을 클라이언트에게 응답
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("success", true);
            return ResponseEntity.ok().body(resultMap);
        } catch (Exception e) {
            // 이메일 전송 실패시 클라이언트에게 오류 응답
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "이메일 전송에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }


    // 인증 코드 검사
    @ResponseBody
    @GetMapping("/email/{code}")
    public ResponseEntity<?> checkEmail(HttpSession session, @PathVariable("code")  String code){

        String sessionCode = (String) session.getAttribute("code");

        if(sessionCode.equals(code)){
            // Json 생성
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", true);

            return ResponseEntity.ok().body(resultMap);
        }else{
            // Json 생성
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", false);

            return ResponseEntity.ok().body(resultMap);
        }
    }

    @GetMapping("/user/findId")
    public String findId(){
        return "/user/findId";
    }
    @PostMapping("/user/findId")
    public ResponseEntity<UserDTO> findId(@RequestBody UserDTO userDTO){
        String name =userDTO.getName();
        log.info("findId...... :" + name);
        String email =userDTO.getEmail();
        UserDTO foundUserDTO = userService.findByNameAndEmail(name, email);

        if (foundUserDTO != null) {
            return ResponseEntity.ok(foundUserDTO);
            // 사용자를 찾은 경우 200 OK 응답으로 사용자 정보 반환
        } else {
            return ResponseEntity.notFound().build();
            // 사용자를 찾지 못한 경우 404 Not Found 응답 반환
        }
    }

    @PostMapping("/user/findIdResult")
    public String findIdResult(String name, String email, Model model){
        UserDTO userDTO = userService.findByNameAndEmail(name, email);
        log.info("result....:" + userDTO.toString());
        model.addAttribute("userDTO" , userDTO);
        log.info(userDTO.toString());

        return "/user/findIdResult";
    }

    @GetMapping("/user/findPassword")
    public String findPassword(){
        return "/user/findPassword";
    }

    @PostMapping("/user/findPassword")
    public ResponseEntity<UserDTO> findPassword(@RequestBody UserDTO userDTO){

        String uid = userDTO.getUid();
        String email = userDTO.getEmail();
        log.info("findPass.....uid: " + uid);
        log.info("findPass.....email: " + email);
        UserDTO foundUserDTO = userService.findPassword(uid, email);
        log.info("findPass...... :" + foundUserDTO);

        if (foundUserDTO != null) {
            return ResponseEntity.ok(foundUserDTO);
            // 사용자를 찾은 경우 200 OK 응답으로 사용자 정보 반환
        } else {
            return ResponseEntity.notFound().build();
            // 사용자를 찾지 못한 경우 404 Not Found 응답 반환
        }

    }

    @PostMapping("/user/findPasswordChange")
    public String findPasswordChange(String uid, String email, Model model){
        UserDTO userDTO = userService.findPassword(uid, email);
        log.info("result....:" + userDTO.toString());
        model.addAttribute("userDTO" , userDTO);
        log.info(userDTO.toString());

        return "/user/findPasswordChange";
    }

    @PutMapping("/updatePass")
    public ResponseEntity<?> putPass(@RequestBody UserDTO userDTO, HttpServletRequest req){
        return userService.updateUserPass(userDTO);
    }

    @PutMapping("/updateZip")
    public ResponseEntity<?> putZip(@RequestBody UserDTO userDTO, HttpServletRequest req){
        log.info(userDTO.toString()+"@@@@");
        return userService.updateUserZip(userDTO);
    }

    @GetMapping("/my/setting")
    public String mySetting(@RequestParam("uid") String uid, Model model){

        UserDTO userDTO = userService.findUserByUid(uid);
        log.info("mySetting......1111:"+userDTO.toString());

        model.addAttribute("userDTO", userDTO);
        log.info("mySetting......:"+userDTO.toString());

        return "/my/setting";
    }

    @ResponseBody
    @GetMapping("/my/setting/{type}/{value}/{uid}")
    public ResponseEntity<?> updateUser(@PathVariable("type") String type,
                                        @PathVariable("value") String value,
                                        @PathVariable("uid") String uid) {
        return userService.updateUser(type, value, uid);
    }

    @DeleteMapping("/user/{uid}")
    public void deleteUser(@PathVariable("uid") String uid){

        userService.deleteUser(uid);

    }



}