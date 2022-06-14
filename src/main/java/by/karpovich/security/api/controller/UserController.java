package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.UserDto;
import by.karpovich.security.api.dto.UserRegistryDto;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(tags = "User Controller")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable(name = "id") Long id) {
        UserDto dto = userService.findById(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/full/{id}")
    public ResponseEntity<User> findB(@PathVariable(name = "id") Long id) {
        User dto = userService.getFullUser(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserDto> result = userService.findAll();

        if (result.isEmpty()) {
            return new ResponseEntity<>("Nothing found for your request", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Save user")
    @PostMapping(value = "/registration")
    public ResponseEntity<?> registration(@RequestPart("user") @Valid UserRegistryDto dto,
                                          @RequestPart("image") MultipartFile file) {
        userService.registration(dto, file);

        return new ResponseEntity<>("User saved successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Update avatar user")
    @PutMapping(value = "/updateAvatar/{id}")
    public ResponseEntity<?> updateAvatar(@PathVariable("id") Long id,
                                          @RequestPart("image") MultipartFile file) {
        userService.updateAvatar(id, file);

        return new ResponseEntity<>("User updated avatar successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Update by id user")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestPart("user") @Valid UserRegistryDto dto,
                                    @PathVariable("id") Long id) {
        userService.update(id, dto);

        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete by id user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

}
