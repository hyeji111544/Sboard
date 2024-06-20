
![slice](https://capsule-render.vercel.app/api?type=slice&color=auto&height=200&text=Sboard!&fontAlign=70&rotate=13&fontAlignY=25&desc=Study%20for%20Spring%20and%20Bootstrap.&descAlign=60&descAlignY=44)


## 프로젝트 소개
- Sboard는 Spring 과 Bootstrap 을 공부하기 위해 진행된 프로젝트 입니다.
- 회원가입을 위해 이메일 인증과 유효성 검사가 진행됩니다.
- 아이디 비밀번호 찾기 기능을 지원합니다.
- 게시판 글 작성과 댓글 등록, 게시글 검색을 할 수 있습니다.
- 개인 정보를 수정하거나 탈퇴가 가능합니다.

## 사용 기술
- **프로그래밍 언어**: Java 17
- **프레임워크**: Spring Boot 3.2.3
- **ORM**: Spring Data JPA, MyBatis 3.0.3
- **보안**: Spring Security, OAuth2 Client
- **템플릿 엔진**: Thymeleaf, Thymeleaf Extras Spring Security6, Thymeleaf Layout Dialect 3.3.0
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL
- **기타**: Lombok, Spring Boot DevTools, Spring Boot Configuration Processor, Spring Boot Starter Mail 3.2.3, ModelMapper 3.2.0, QueryDSL 5.1.0


## 프로젝트 진행하며
### 여러가지 쿼리 작성
- 해당 프로젝트는 MyBatis, JPA, QueryDSL 세가지로 쿼리문이 작성되어 있습니다.
  이는 프로젝트를 처음 시작하며 여러가지 문법에 익숙해 지기 위한 방법이었고,
  MyBatis 와 QueryDSL 로 동적 쿼리를 만들어 보며 적절한 사용방법을 고민해 볼 수 있었습니다.

### 비밀번호 암호화
- SHA-256 는 동일 입력에 대해 항상 동일한 결과가 출력되며 보안이 취약하다는 점이 있어
  Spring Security의 PasswordEncoder를 사용하여 비밀번호 암호화를 하였습니다.

### Bootstrap 사용
- Bootstrap을 사용하여 반응형 프론트 개발과
  스피너, 호버 등 적절한 요소를 활용하는 방법을 배웠습니다.

### API 활용
<img src="https://github.com/hyeji111544/Sboard/assets/154953972/6e4087cc-edfc-471a-adfc-2639675f6e0d" width="400"/>
- 주소 검색을 위해 kakao에서 제공하는 API 를 활용하기 위해 API 에 대해 공부하여 기능 구현을 할 수 있었습니다.
  
### PageResponseDTO 생성
- 게시판의 필수적인 기능인 페이지네이션 기능을 만들기 위하여 게시글의 길이, 보일 페이지 등 여러가지 요소를
  한번에 관리하여 값을 리턴시켜주는 DTO 를 만들어 DTO 의 활용 방법을 알 수 있었습니다.

## 페이지별 기능

### [초기화면]
- 서비스 접속 시 로드되는 화면으로 로그인 또는 회원가입을 진행합니다.
  - SNS 로그인 기능은 구현되어 있지 않습니다.

### [회원가입]
- 약관동의후 아이디, 닉네임, 이메일 중복 검사를 합니다.
- 등록한 이메일로 인증 코드 전송 후 이메일 인증이 통과되면 가입됩니다.

![1](https://github.com/hyeji111544/Sboard/assets/154953972/a4447015-e778-4c3c-bea5-14925a0f305e)

### [개인정보 수정]
- 아이디를 제외한 개인 정보를 수정할 수 있습니다.

![2](https://github.com/hyeji111544/Sboard/assets/154953972/7b02f601-69ff-4e59-9ef3-4d6c7e5b62f8)

### [게시판]
- 게시판 조회 및 글 작성/수정 , 댓글 작성이 가능합니다.

![3](https://github.com/hyeji111544/Sboard/assets/154953972/59c7c087-c396-4f6b-b535-b9a038b7319f)

### [아이디/비밀번호 찾기]
- 화면에 표시된 정보 입력 후 이메일 인증을 통해 아이디를 찾을 수 있습니다.
- 비밀번호의 경우는 변경 후 변경된 비밀번호를 통해 로그인이 가능합니다.

![4](https://github.com/hyeji111544/Sboard/assets/154953972/8e54c54a-6c24-460b-b19f-4367dc5f0fe0)
