package org.otomotus.backend.controller;

import org.otomotus.backend.dto.UserDto;
import org.otomotus.backend.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

//    Tworzenie usera przez admina
//    @PostMapping
//    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
//        return ResponseEntity.ok(userService.create(userDto));
//    }


}
