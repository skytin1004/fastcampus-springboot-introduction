package com.example.resttemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // getter ,setter ,toString 등등 bean과 관련된 모든 코드 모음.
@NoArgsConstructor // 어떠한 매개변수도 받지 않는 기본 생성자 생성.
@AllArgsConstructor //모든 맴버변수를 매개변수로 받는 생성자 생성.
public class User {

    private String name;
    private int age;
    private List<Car> carList;
}
