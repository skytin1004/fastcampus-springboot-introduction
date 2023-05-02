package com.example.exception.dto;

public class Error {

    private String field;
    private String message;
    private String invalidValue;

    //각 매개변수의 getter,setter 생성 - 롬복을 이용해서 간단히 생성할 수 도 있다.

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(String invalidValue) {
        this.invalidValue = invalidValue;
    }
}
