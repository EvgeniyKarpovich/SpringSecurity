package by.karpovich.security.service;

import by.karpovich.security.api.dto.UserDto;
import by.karpovich.security.api.dto.UserRegistryDto;
import by.karpovich.security.exception.DuplicateException;
import by.karpovich.security.exception.NotFoundModelException;
import by.karpovich.security.jpa.model.Role;
import by.karpovich.security.jpa.model.Status;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.RoleRepository;
import by.karpovich.security.jpa.repository.UserRepository;
import by.karpovich.security.mapping.UserMapper;
import by.karpovich.security.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Value("${upload.path}")
    private String uploadPath;

    public void registration(UserRegistryDto dto, MultipartFile file) {

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        String resultFileName = "";

        if (file != null && !file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            resultFileName = uuidFile + "." + file.getOriginalFilename();
        }

        validateAlreadyExistsRegistry(null, dto);
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRoles(userRoles);
        user.setAvatar(resultFileName);

        User registeredUser = userRepository.save(user);

        String uploadDir = uploadPath;

        try {
            FileUploadUtil.saveFile(uploadDir, resultFileName, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("IN registration - user: {} successfully registered", registeredUser);
    }

    public void update(Long id, UserRegistryDto dto) {
        validateAlreadyExistsRegistry(id, dto);

        User user = new User();
        user.setId(id);
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        User registeredUser = userRepository.save(user);

        log.info("IN update -  User  '{}' , updated", registeredUser);
    }

    public void updateAvatar(Long id, MultipartFile file) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(
                () -> new NotFoundModelException(String.format("User with id = %s not found", id)));
        String resultFileName = "";

        if (file != null && !file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            resultFileName = uuidFile + "." + file.getOriginalFilename();
        }

        user.setId(id);
        user.setAvatar(resultFileName);
        userRepository.save(user);

        String uploadDir = uploadPath;

        try {
            FileUploadUtil.saveFile(uploadDir, resultFileName, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserDto findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        User userModel = user.orElseThrow(
                () -> new NotFoundModelException(String.format("User with id = %s not found", id)));
        log.info("IN findById -  User with id = {} found", userModel.getId());
        return userMapper.mapFromEntity(userModel);
    }

    public void deleteById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(String.format("User with id = %s not found", id));
        }
    }

    public User findByLogin(String login) {
        User model = userRepository.findByLogin(login);
        log.info("IN findByLogin -  User with login = {} found", model.getId());
        return model;
    }

    public List<UserDto> findAll() {
        List<User> userModels = userRepository.findAll();
        log.info("IN findAll - the number of actors according to these criteria = {}", userModels.size());
        return userMapper.mapFromListEntity(userModels);
    }

    private void validateAlreadyExists(Long id, UserDto dto) {
        Optional<User> check = userRepository.findByEmail(dto.getEmail());
        if (check.isPresent() && !Objects.equals(check.get().getId(), id)) {
            throw new DuplicateException(String.format("User with id = %s already exist", id));
        }
    }

    private void validateAlreadyExistsRegistry(Long id, UserRegistryDto dto) {
        Optional<User> check = userRepository.findByEmail(dto.getEmail());
        if (check.isPresent() && !Objects.equals(check.get().getId(), id)) {
            throw new DuplicateException(String.format("User with id = %s already exist", id));
        }
    }

}
