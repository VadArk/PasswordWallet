package com.varkaikin.passwordwallet.controller;
import com.varkaikin.passwordwallet.dto.DataChangeResponse;
import com.varkaikin.passwordwallet.service.DataChangeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/datachange")
public class DataChangeController {

    private final DataChangeService dataChangeService;

    public DataChangeController(DataChangeService dataChangeService) {

        this.dataChangeService = dataChangeService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DataChangeResponse>> getAllByLogin(@RequestParam("login") String login,
                                                                  @RequestParam("passwordId") String passwordId){
        if (login == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        List<DataChangeResponse> dataChangeResponses= dataChangeService.findAllByUserLoginAndPasswordId(login,passwordId);
        return new ResponseEntity<>(dataChangeResponses, headers, HttpStatus.OK);

    }


}
