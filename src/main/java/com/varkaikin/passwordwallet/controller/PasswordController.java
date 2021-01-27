package com.varkaikin.passwordwallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.varkaikin.passwordwallet.service.PasswordService;
import com.varkaikin.passwordwallet.dto.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {

        this.passwordService = passwordService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<PasswordRequest> save(@RequestBody AddPasswordRequest passwordRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (passwordRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        PasswordRequest add = this.passwordService.add(passwordRequest);
        return new ResponseEntity<>(add, headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/share")
    public ResponseEntity<Boolean> share(@RequestBody SharePassRequest sharePassRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (sharePassRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean add = passwordService.share(sharePassRequest);
        return new ResponseEntity<>(add, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<Boolean> edit(@RequestBody EditRequest editPassRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (editPassRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean add = passwordService.edit(editPassRequest);
        return new ResponseEntity<>(add, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("passwordId") String passwordId,
                                          @RequestParam("userLogin")  String userLogin  ) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (passwordId == null || userLogin == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean deleted = passwordService.delete(passwordId, userLogin);
        return new ResponseEntity<>(deleted, headers, HttpStatus.OK);

    }

    @PostMapping(value = "/show")
    public ResponseEntity<String> showPassword(@RequestBody ShowPasswordRequest showPasswordRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (showPasswordRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String password = passwordService.showPassword(showPasswordRequest);
        return new ResponseEntity<>(password, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/allbylogin")
    public ResponseEntity<PasswordResponse> getAllByLogin(@RequestParam String login,
                                                          @RequestParam String pageNumber,
                                                          @RequestParam String pageSize) {
        HttpHeaders headers = new HttpHeaders();
        if (login == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
        PasswordResponse resp = passwordService.findAllByUserLogin(login, pageable);
        return new ResponseEntity<>(resp, headers, HttpStatus.OK);

    }
    @GetMapping(value = "/id")
    public ResponseEntity<OnePasswordResponse> getPasswordById(@RequestParam String id,
                                                               @RequestParam String login ) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        OnePasswordResponse resp = passwordService.fidPasswordById(id, login);
        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
    }

    @PutMapping(value = "/restore")
    public ResponseEntity<Boolean> restore(@RequestParam("changeId") String changeId,
                                           @RequestParam("login") String login
    ) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        if (changeId == null || login == null  ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean restored = passwordService.restore(changeId, login);
        return new ResponseEntity<>(restored, headers, HttpStatus.OK);

    }





}
