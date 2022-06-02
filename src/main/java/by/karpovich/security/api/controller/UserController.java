package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.UserDto;
import by.karpovich.security.api.dto.UserRegistryDto;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable(name = "id") Long id) {
        UserDto user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserDto> result = userService.findAll();

        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Save user")
    @PostMapping(value = "/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid UserRegistryDto dto) {
        User registration = userService.registration(dto);
        if (registration == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User saved successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Update by id user")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto dto,
                       @PathVariable("id") Long id) {
        UserDto update = userService.update(id, dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete by id user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

}
