package com.example.exception.controller;

import com.example.exception.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController //RestApi를 이용하는  Controller라는 뜻
@RequestMapping("/api/user") // api/user으로 주소 할당
@Validated // 검증할때 사용하는 어노테이션(Valid,Validated)가 있음, 공부!!
public class ApiController {

    @GetMapping("") // ?name=1234 // 그대로 주소 할당
    public User get(
            @Size(min = 2)
            @RequestParam String name, // name 매개변수(파라미터)에는 size의 최소가 2
            //Size(min=1) 과 Min(1)의 차이? : Size는 문자열 길이를 의미하는것. Min은 숫자의 최솟값.

            @NotNull
            @Min(1)
            @RequestParam Integer age){ // age 매개변수(파라미터)에는 
        User user = new User();
        user.setName(name);
        user.setAge(age);

        return user;
    }


    @PostMapping("")// 그대로 주소 할당
    public User post(@Valid @RequestBody User user){ // Valid를 사용한 검증방식 -> API 의 Request부분에 집어넣는다. , PostMapping 이기 때문에 Body에 들어갈 값을 입력한다.
        System.out.println(user);
        return user;
    }

}
