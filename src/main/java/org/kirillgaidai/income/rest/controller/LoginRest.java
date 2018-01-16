package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.login.LoginRequestRestDto;
import org.kirillgaidai.income.rest.dto.login.LoginResponseRestDto;
import org.kirillgaidai.income.service.dto.UserDto;
import org.kirillgaidai.income.service.intf.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginRest {

    final private static Logger LOGGER = LoggerFactory.getLogger(LoginRest.class);

    final private IUserService userService;

    @Autowired
    public LoginRest(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseRestDto> login(@Valid @RequestBody LoginRequestRestDto request) {
        LOGGER.debug("Entering method");
        UserDto dto = new UserDto(null, request.getLogin(), request.getPassword(), null, null, null, null);
        dto = userService.login(dto);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        LoginResponseRestDto response = new LoginResponseRestDto(dto.getToken(), dto.getExpires());
        return ResponseEntity.ok(response);
    }

}
