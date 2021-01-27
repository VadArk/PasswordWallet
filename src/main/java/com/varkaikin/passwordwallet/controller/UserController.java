package com.varkaikin.passwordwallet.controller;

import com.varkaikin.passwordwallet.model.Log;
import com.varkaikin.passwordwallet.security.JwtUtils;
import com.varkaikin.passwordwallet.service.LogService;
import com.varkaikin.passwordwallet.service.UserService;
import com.varkaikin.passwordwallet.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final LogService logService;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<UserResponse> save(@RequestBody UserDTORegister userDTORegister) {
        if (userDTORegister == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByLogin(userDTORegister.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserResponse());
        }

        HttpHeaders headers = new HttpHeaders();
        UserResponse add = userService.add(userDTORegister);
        return new ResponseEntity<>(add, headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO userDTO,
                                               HttpServletRequest request) {
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!userService.existsByLogin(userDTO.getLogin())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        HttpHeaders headers = new HttpHeaders();
        String remoteAddr = request.getRemoteAddr();
        Boolean result = userService.login(userDTO);

        Integer status = logService.getLoginStatus(userDTO.getLogin(), result, remoteAddr);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(status.toString());
        Log lastSuccessful = logService.findLastSuccessful(remoteAddr, userDTO.getLogin());
        Log lastUnSuccessful = logService.findLastUnSuccessful(remoteAddr, userDTO.getLogin());

        if(lastSuccessful!=null)
        loginResponse.setLastSuccess(lastSuccessful.getTime() );
        else  loginResponse.setLastSuccess(null);

        if(lastUnSuccessful!=null)
            loginResponse.setLastUnsuccessful(lastUnSuccessful.getTime() );
        else  loginResponse.setLastUnsuccessful(null);



        return ResponseEntity.ok(loginResponse);

    }

    @PostMapping(value = "/reset")
    public ResponseEntity<Boolean> resetIp(HttpServletRequest request) {

        String remoteAddr = request.getRemoteAddr();
        logService.resetIp(remoteAddr);


        return ResponseEntity.ok(true);

    }

    @PostMapping(value = "/change")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeUserPasswordRequest uRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (uRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean changeResult = this.userService.changePassword(uRequest);


        return new ResponseEntity<>(changeResult, headers, HttpStatus.OK);


    }


}
