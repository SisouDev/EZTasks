package com.spring.EZTasks.controller.user;

import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.utils.enums.Sector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @GetMapping
    public ResponseEntity<UserDTO> findAll(){
        UserDTO userDTO = new UserDTO(
                "teste@gmail.com", 1L, "teste",
                "teste123", "eng",
                Sector.ENG
        );
        return ResponseEntity.ok().body(userDTO);
    }
}
