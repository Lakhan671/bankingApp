package com.bank.controller;
import com.bank.model.ResponseData;
import com.bank.model.security.AuthRequest;
import com.bank.model.security.AuthResponse;
import com.bank.security.JWTUtil;
import com.bank.security.PBKDF2Encoder;
import com.bank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.WeakHashMap;

/**

 The AuthController class handles the authentication-related endpoints for user login and registration.
 It provides the necessary API endpoints to authenticate users and manage user authentication tokens.
 Endpoints:
 /api/login/v1: POST endpoint for user login. Accepts the user's credentials and returns an authentication token upon successful login.
 api/signup/v1: POST endpoint for user registration. Accepts the user's information and creates a new user account.
 /user/get/v1: POST endpoint for user information
 @author Lakhan Singh
 */

@RestController
@RequestMapping(value = "/api")
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PBKDF2Encoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping("/login/v1")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.findByUsername(authRequest.getUsername())
                .filter(userDetails -> passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword()))
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }



    @PostMapping(value = "/signup/v1")
    public Mono<ResponseEntity<ResponseData>> addTransaction(@RequestBody WeakHashMap<String,String> request) {
        log.info(" signup  request  ={}", request);
        return userService.saveUser(request)
                .doOnSuccess(response -> log.info(" request for signup={} completed successfully", response))
                .map(res -> ResponseEntity
                        .accepted()
                        .body(ResponseData.builder().msg("you have signup successfully....").build()))
                .onErrorResume(e -> {
                    Throwable throwable = (Throwable) e;
                    log.info("Signup error", throwable.getMessage());
                    return Mono.just(ResponseEntity
                            .accepted()
                            .body(ResponseData.builder().msg("Data duplication was discovered. Please try again with a new email address.").build()));
                });



    }

    @PostMapping("/user/get/v1")
    public Mono<ResponseEntity<ResponseData>> getUser(@RequestBody WeakHashMap<String,String>request){
        log.info(" user  request  ={}", request);
        return userService.getUser(request.get("email"))
                .doOnSuccess(response -> log.info(" request for user details={} completed successfully", response))
                .map(res -> ResponseEntity
                        .accepted()
                        .body(ResponseData.builder().msg("User information has successfully received....").data(res).build()))
                .onErrorResume(e -> {
                    Throwable throwable = (Throwable) e;
                    log.info("Error ", throwable.getMessage());
                    return Mono.just(ResponseEntity
                            .accepted()
                            .body(ResponseData.builder().msg("Request is invalid....").build()));
                });
    }
}
