package com.example.resttemplate.service;

import com.example.resttemplate.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j //스프링부트에서 로그를 남길 때 사용하는 어노테이션 (Simple Logging Facade for Java)
@Service  // 해당 클래스를 루트 컨테이너에 빈(Bean) 객체로 생성해주는 어노테이션
public class UserService {

    public void getForObject(){
        URI uri = UriComponentsBuilder // 스프링에서 URI를 쉽게 만들어주는 어노테이션 (URI 의 구성요소만 입력하면 URI를 만들어준다.)
                .fromUriString("http://localhost:9090")
                .path("/api")
                .queryParam("name","steve")
                .queryParam("age",10)
                .encode()
                .build()
                .toUri();
        log.info("uri : {}",uri);

        RestTemplate restTemplate = new RestTemplate(); // RestTemplate : Rest API를 호출할 수 있는 Spring 내장 클래스
        User user = restTemplate.getForObject(uri, User.class); //restTemplate.getForObject: 주어진 URL 주소로 HTTP GET 메서드에 의한 결과를 반환한다. 
        log.info("user : {}", user);
    }

    public void getForEntity(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/{path}")
                .queryParam("name","steve")
                .queryParam("age",10)
                .encode()
                .build()
                .expand("user")
                .toUri();
        log.info("uri : {}", uri);

        RestTemplate restTemplate = new RestTemplate();// RestTemplate : Rest API를 호출할 수 있는 Spring 내장 클래스
        // ResponseEntity<User> response = restTemplate.getForEntity(uri, User.class); //주어진 URL 주소로 HTTP GET 메서드를 거쳐 ResponseEntity로 결과를 반환받는다
        log.info("{}",response.getStatusCode()); //기록하기.. 추가로 공부
        log.info("{}",response.getHeaders());
        log.info("{}",response.getBody());
    }

    public void postForObject(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/{path}")
                .encode()
                .build()
                .expand("user")
                .toUri();
        log.info("uri : {}", uri);

        RestTemplate restTemplate = new RestTemplate();

        User user = new User();
        user.setName("홍길동");
        user.setAge(10);
        User response = restTemplate.postForObject(uri, user, User.class);// POST 요청을 보내고 객체로 결과를 반환한다.
        log.info("response : {}", response);
    }
 
    public void postForEntity(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/{path}")
                .encode()
                .build()
                .expand("user")
                .toUri();
        log.info("uri : {}", uri);

        User user = new User();
        user.setName("홍길동");
        user.setAge(10);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response = restTemplate.postForEntity(uri, user, User.class); 
        log.info("{}",response.getStatusCode());
        log.info("{}",response.getHeaders());
        log.info("{}",response.getBody());
    }

    public void exchange(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/{path}/header")
                .encode()
                .build()
                .expand("user")
                .toUri();
        log.info("uri : {}", uri);

        User user = new User();
        user.setName("홍길동");
        user.setAge(10);

        RequestEntity<User> req = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization","my-header")
                .body(user);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response = restTemplate.exchange(req, new ParameterizedTypeReference<>(){});
        log.info("{}",response.getStatusCode());
        log.info("{}",response.getHeaders());
        log.info("{}",response.getBody());
    }



    public void naver(){
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query","%EC%A3%BC%EC%8B%9D")
                .queryParam("display","10")
                .queryParam("start","1")
                .queryParam("sort","random")
                .encode()
                .build()
                .toUri();
        log.info("uri : {}", uri);

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id","Zi3o1uQftp59zuIqEAz4")
                .header("X-Naver-Client-Secret","iy6YKSWpLM")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(req, new ParameterizedTypeReference<>(){});
        log.info("{}",response.getStatusCode());
        log.info("{}",response.getHeaders());
        log.info("{}",response.getBody());
    }
}
