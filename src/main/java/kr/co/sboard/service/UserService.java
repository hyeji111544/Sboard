package kr.co.sboard.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.TermsDTO;
import kr.co.sboard.dto.UserDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.entity.User;
import kr.co.sboard.mapper.UserMapper;
import kr.co.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    // JavaMailSender 주입
    private final JavaMailSender javaMailSender;

    public TermsDTO selectTerms(){
        return userMapper.selectTerms();
    }

    public void insertUser(UserDTO userDTO){

        String encoded = passwordEncoder.encode(userDTO.getPass());
        userDTO.setPass(encoded);

        userMapper.insertUser(userDTO);
    }

    public int selectCountUser(String type, String value) {
        log.info("count.... : " + type);
        log.info("count.... : " + value);
        return userMapper.selectCountUser(type, value);
    }

    public ResponseEntity<?> updateUser(String type, String value, String uid){
        userMapper.updateUserBy(type, value, uid);

        // 업데이트된 사용자 정보 조회
        Optional<User> updatedUserOptional = userRepository.findById(uid);

        // 업데이트된 사용자 정보가 존재하는지 확인
        if (updatedUserOptional.isPresent()) {
            // 업데이트된 사용자 정보를 ResponseEntity에 담아 반환
            User updatedUser = updatedUserOptional.get();
            return ResponseEntity.ok(updatedUser);
        } else {
            // 업데이트된 사용자 정보가 존재하지 않을 경우 404 에러 반환
            return ResponseEntity.notFound().build();
        }
    }

    public UserDTO selectUser(String uid){
        Optional<User> optUser = userRepository.findById(uid);
        log.info("findById ...1 "+ optUser);

        UserDTO userDTO = null;
        if (optUser.isPresent()){
            User user = optUser.get();
            log.info("findById ...2 " + user.toString());

            userDTO = modelMapper.map(user, UserDTO.class);
            log.info("findById ...3 " + userDTO);
        }

        return userDTO;

    }

    public UserDTO findByNameAndEmail(String name, String email){
        log.info("name :" + name);
        log.info("email :" + email);
        Optional<User> optUser = userRepository.findUserByNameAndEmail(name, email);
        UserDTO userDTO = null;

        log.info("findUser..." + optUser);

        if (optUser.isPresent()){
            User user = optUser.get();
            userDTO = modelMapper.map(user, UserDTO.class);

            return userDTO;
        }else {
            return null; // 또는 예외를 던지거나 다른 방식으로 처리
        }
    }

    public UserDTO findPassword(String uid, String email){
        Optional<User> optUser = userRepository.findUserByUidAndEmail(uid, email);
        UserDTO userDTO = null;

        log.info("findpass..." + optUser);

        if (optUser.isPresent()){
            User user = optUser.get();
            userDTO = modelMapper.map(user, UserDTO.class);


            return userDTO;
        }else {
            return null; // 또는 예외를 던지거나 다른 방식으로 처리
        }
    }

    public UserDTO findUserByUid(String uid){
        Optional<User> optUser = userRepository.findById(uid);
        log.info("findUserByUid..... :");
        UserDTO userDTO = null;

        log.info("findUserByUid.....1 :" + optUser);

        if (optUser.isPresent()){
            User user = optUser.get();
            userDTO = modelMapper.map(user, UserDTO.class);
            log.info("findUserByUid......2 :" + userDTO.toString());


            return userDTO;
        }else {
            return null; // 또는 예외를 던지거나 다른 방식으로 처리
        }
    }


    public ResponseEntity<?> updateUserPass(UserDTO userDTO){
        Optional<User> optUser = userRepository.findById(userDTO.getUid());

        if (optUser.isPresent()){
            User user = optUser.get();

            String encoded = passwordEncoder.encode(userDTO.getPass());
            log.info(encoded);
            user.setPass(encoded);


            log.info("updateUser....."+ user);

            User updateUser = userRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updateUser);
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }
    }

    public ResponseEntity<?> updateUserZip(UserDTO userDTO){
        Optional<User> optUser = userRepository.findById(userDTO.getUid());

        if (optUser.isPresent()){

            userMapper.updateUserZip(userDTO);
            Map<String, Object> jsonResult = new HashMap<>();
            jsonResult.put("result","success");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(jsonResult);
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }
    }

    public void deleteUser(String uid){
        //삭제 전 조회
        Optional<User> optUser = userRepository.findById(uid);

        log.info("optUser.... : " + optUser);

        if(optUser.isPresent()){
            log.info("deleteUser.....1");

        // 유저 id 와 leaveDate 만 남기고 나머지 null 처리
            userMapper.deleteUser(uid);

        }else {
            log.info("deleteUser.....2");

        }
    }


    /*
        - build.gradle 파일에 spring-boot-starter-mail 의존성 추가 할것
        - application.yml 파일 spring email 설정 추가
     */
    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmailCode(HttpSession session, String receiver){
        log.info("sender : " + sender);

        // MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        // 인증코드 생성 후 세션 저장
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        session.setAttribute("code", String.valueOf(code));

        String title = "sboard 인증코드입니다.";
        String content = "<h1>인증코드는" + code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            javaMailSender.send(message);
        }catch(Exception e){
            log.error("sendEmailConde : " + e.getMessage());
        }


    }


}