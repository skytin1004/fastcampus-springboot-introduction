package com.example.resttemplate.controller;

import com.example.resttemplate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor // final이나 @NonNull인 값만 파라미터로 받는 생성자를 자동으로 생성한다. - 클래스의 기본 생성자를 자동으로 생성해주는 어노테이션임.
public class UserApiController {

    private final UserService userService;  

    //@RequiredArgsConstructor 을 사용하지 않았다면 
    //@Autowired // - 의존성 주입할 때 사용하는 어노테이션 - 주로 생성자 위에 사용한다. - 추가적인 공부 필요..!
    //public UserApiController(UserService userService) {
    //this.userService = userService; } 이렇게 생성자를 직접 만들어야 한다.

    @GetMapping("")
    public void get(){
        userService.naver();
    }

}
