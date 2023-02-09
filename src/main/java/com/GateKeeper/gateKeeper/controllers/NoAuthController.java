package com.GateKeeper.gateKeeper.controllers;

import com.GateKeeper.gateKeeper.model.Role;
import com.GateKeeper.gateKeeper.model.UserDTO;
import com.GateKeeper.gateKeeper.services.UserService;
import com.GateKeeper.gateKeeper.services.UtilityService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class NoAuthController {

    private static final Logger logger = LoggerFactory.getLogger(NoAuthController.class);
    private final UserService userService;
    private final UtilityService utilityService;

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> register(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult){

        if(utilityService.errorsFound(bindingResult))
            return ResponseEntity.badRequest().body("Send valid User Details");
        else if(userService.userNameExists(userDTO.getUsername()) || userService.emailExists(userDTO.getEmail())){
            logger.error("The User already exists");
            return ResponseEntity.badRequest().body("Username or Email are linked to an existing user.");
        }
        if(userDTO.getRole()==null)
            userDTO.setRole(Role.USER);
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }
}



//    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<UserDTO> register(@RequestParam Map<String,String> map){
//        UserDTO userDTO = new UserDTO();
//        if(map.size()!=4)
//        {
//            return ResponseEntity.badRequest().body(userDTO);
//        }
//        userDTO.setUsername(map.get("username"));
//        userDTO.setPassword(map.get("password"));
//        userDTO.setEmail(map.get("email"));
//        userDTO.setRole(Role.valueOf(map.get("role")));
//        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
//    }