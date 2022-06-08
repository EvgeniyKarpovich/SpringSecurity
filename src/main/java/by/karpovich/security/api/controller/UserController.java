package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.UserDto;
import by.karpovich.security.api.dto.UserRegistryDto;
import by.karpovich.security.exception.DuplicateException;
import by.karpovich.security.jpa.model.Role;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.RoleRepository;
import by.karpovich.security.jpa.repository.UserRepository;
import by.karpovich.security.service.UserService;
import by.karpovich.security.util.FileUploadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/users")
@Api(tags = "User Controller")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable(name = "id") Long id) {
        UserDto user = userService.findById(id);

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

//    @ApiOperation(value = "Save user")
//    @PostMapping(value = "/registration")
//    public ResponseEntity<?> registration(@RequestBody @Valid UserRegistryDto dto) {
//        User registration = userService.registration(dto);
//
//        if (registration == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>("User saved successfully", HttpStatus.OK);
//    }

//    @ApiOperation(value = "Save user")
//    @PostMapping(value = "/registration")
//    public ResponseEntity<?> registration(UserRegistryDto dto,
//                                          @RequestParam("image") MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//
//        Role roleUser = roleRepository.findByName("ROLE_USER");
//        List<Role> userRoles = new ArrayList<>();
//        userRoles.add(roleUser);
//
//        validateAlreadyExistsRegistry(null, dto);
//        User user = new User();
//        user.setLogin(dto.getLogin());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setStatus(Status.ACTIVE);
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
//        user.setEmail(dto.getEmail());
//        user.setRoles(userRoles);
//        user.setAvatar(fileName);
//
//        User savedUser = userRepository.save(user);
//
//        String uploadDir = "C://test//" + savedUser.getId();
//
//        FileUploadUtil.saveFile(uploadDir, fileName, file);
//
//        return new ResponseEntity<>("User saved successfully", HttpStatus.OK);
//    }

    @ApiOperation(value = "Save user")
    @PostMapping(value = "/registration")
    public ResponseEntity<?> registration(User user,
                                          @RequestParam("image") MultipartFile file) throws IOException {
        String resultFileName = "";

        if (file != null && !file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
             resultFileName = uuidFile + "." + file.getOriginalFilename();
        }

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setLogin("zaebalo");
        user.setPassword("zaebalo");
        user.setFirstName("zaebalo");
        user.setLastName("zaebalo");
        user.setEmail("zlytyytyo");
        user.setAvatar(resultFileName);

        userRepository.save(user);

        String uploadDir = uploadPath;

        FileUploadUtil.saveFile(uploadDir, resultFileName, file);

        return new ResponseEntity<>("User saved successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Update by id user")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto dto,
                                    @PathVariable("id") Long id) {
        UserDto update = userService.update(id, dto);

        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete by id user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    private void validateAlreadyExistsRegistry(Long id, UserRegistryDto dto) {
        Optional<User> check = userRepository.findByEmail(dto.getEmail());
        if (check.isPresent() && !Objects.equals(check.get().getId(), id)) {
            throw new DuplicateException(String.format("User with id = %s already exist", id));
        }
    }

}
