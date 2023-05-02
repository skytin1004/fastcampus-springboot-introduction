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
        System.out.println(e.getClass().getName()); //오류의 패키지 이름이 포함된 클래스 이름을 출력한다.

        //서버 에러를 표시한다.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(""); //반환값에 상태코드와 응답 메세지를 주고 싶을 때 사용되는ResponseEntity
    }
    // methodArgumentNotValidException: Json 파싱이 제대로 이루어진 후 @Valid 단계에서 오류가 있는 경우 발생
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //MethodArgumentNotValidException.class의 예외가 발생했을 때 해당 메소드 작동
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>(); //새로운 리스트 생성.

        BindingResult bindingResult = e.getBindingResult(); // getBindingResult():해당 BindingResult에서 발생한 에러를 가져온다.
        bindingResult.getAllErrors().forEach(error -> { //해당 BindingResult에서 발생한 에러를 모두 가져와 foreach를 통해 작업한다.
            FieldError field = (FieldError) error;

            String fieldName = field.getField(); //  cls.getField("string1") -> getField의 리턴 예시: Public field found: public java.lang.String ClassDemo.string1 
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString();

            Error errorMessage = new Error(); //Error 클래스의 객체 생성
            errorMessage.setField(fieldName); // fieldName 설정
            errorMessage.setMessage(message); // message 설정
            errorMessage.setInvalidValue(value); // InvalidValue 설정

            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(); // ErrorResponse 클래스의 객체 생성.
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI()); //URI설정
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString()); // http상태 코드를 에러 코드로 전달 -> 문자열로 변환해서.
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse); //body부분에  errorResponse담아서 에러코드 전달.
    }

    @ExceptionHandler(value = ConstraintViolationException.class) //ConstraintViolationException.class의 에러가 생기면 아래 메소드대로 처리.
    public ResponseEntity constraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest){ //예를 들어회원 정보를 컨트롤러로 보내면 HttpServletRequest 객체 안에 모든 데이터들이 들어있게됨.

        List<Error> errorList = new ArrayList<>(); // errorList list 생성

        e.getConstraintViolations().forEach(error ->{ 
            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false); // 잘 모르겠음..
            List<Path.Node> list = stream.collect(Collectors.toList()); //스트림을 리스트로 변환하는 방법: 1.collect(Collectors.toList()), 2.Stream.toList()

            String field = list.get(list.size() -1).getName(); // alist.get(index) : python = alist[index] - 객체를 가져오는 메소드 / getName은 getClass의 getName인가..?
            String message = error.getMessage(); // 에러의 원인을 간단히 출력해주는 메소드
            String invalidValue = error.getInvalidValue().toString(); // 잘 모르겠음..

            Error errorMessage = new Error();
            errorMessage.setField(field);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(invalidValue);

            errorList.add(errorMessage);

        });

        ErrorResponse errorResponse = new ErrorResponse(); // 아래도 똑같이 어떠한 종류의 에러가 발생했을 때 실행할 메서드를 적어놓았다.
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
