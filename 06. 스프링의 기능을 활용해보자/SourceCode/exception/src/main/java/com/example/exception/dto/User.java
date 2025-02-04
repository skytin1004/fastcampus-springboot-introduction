package com.example.exception.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

    // User클래스의 맴버 변수 지정.
    // validation 어노테이션
    @NotEmpty // 공란 제출 불가 (null , "" 불가)
    @Size(min = 1, max = 10) // 문자열 길이 1~10
    private String name;

    @Min(1) // 최솟값 지정
    @NotNull //null값 불가
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
