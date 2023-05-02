package com.example.exception.advice;

import com.example.exception.controller.ApiController;
import com.example.exception.dto.Error;
import com.example.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
// 예외처리할 때 사용하는 어노테이션 @RestControllerAdvice , @ControllerAdvice
// 둘 다 예외처리할 때 사용하는 어노테이션이지만 RestControllerAdvice는 예외처리에 대한 응답으로 객체를 리턴하는 기능이 추가로 있다. -> ResponseEntity를 이용해서 객체 리턴
@RestControllerAdvice(basePackageClasses = ApiController.class) //ApiController에서 발생하는 예외를 처리한다는 뜻.
public class ApiControllerAdvice {   

    //ExceptionHandler : 예외가 발생했을 때 ExceptionHandler에서 지정한 방식으로 예외를 처리하게 해주는 기능.(메소드에서 지정한 로직으로 예외 처리)
    @ExceptionHandler(value = Exception.class) // Exception클래스의 예외가 발생했을 때 해당 메소드 작동 (모든 자바의 예외 클래스는 Exception.class의 상속을 받는다.)
    public ResponseEntity exception(Exception e){ 
        //getClass().getName : 패키지 이름이 포함된 클래스 이름. getClass().getSimpleName(): 패키지 경로가 포함되지 않은 클래스이름
        System.out.println(e.getClass().getName()); //오류의 클래스와 이름을 출력한다.

        //서버 에러를 표시한다.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(""); //반환값에 상태코드와 응답 메세지를 주고 싶을 때 사용되는ResponseEntity
    }
    // methodArgumentNotValidException: Json 파싱이 제대로 이루어진 후 @Valid 단계에서 오류가 있는 경우 발생
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //MethodArgumentNotValidException.class의 예외가 발생했을 때 해당 메소드 작동
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>(); //새로운 리스트 생성.

        BindingResult bindingResult = e.getBindingResult(); // 여기부터
        bindingResult.getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;

            String fieldName = field.getField();
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString();

            Error errorMessage = new Error();
            errorMessage.setField(fieldName);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(value);

            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>();

        e.getConstraintViolations().forEach(error ->{
            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());

            String field = list.get(list.size() -1).getName();
            String message = error.getMessage();
            String invalidValue = error.getInvalidValue().toString();

            Error errorMessage = new Error();
            errorMessage.setField(field);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(invalidValue);

            errorList.add(errorMessage);

        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>();

        String fieldName = e.getParameterName();
        String invalidValue = e.getMessage();

        Error errorMessage = new Error();
        errorMessage.setField(fieldName);
        errorMessage.setMessage(e.getMessage());


        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
