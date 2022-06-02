package by.karpovich.security.service;

import by.karpovich.security.jpa.model.Role;
import by.karpovich.security.jpa.model.Status;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.RoleRepository;
import by.karpovich.security.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }
        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    public void deleteById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(String.format("User with id = %s not found", id));
        }
        log.info("IN deleteById - User with id = {} deleted", id);
    }


//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    public UserModel register(UserModel user) {
//        Role roleUser = roleRepository.findByName("ROLE_USER");
//        List<Role> userRoles = new ArrayList<>();
//        userRoles.add(roleUser);
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(userRoles);
//        user.setStatus(Status.ACTIVE);
//        UserModel registeredUser = userRepository.save(user);
//
//        log.info("IN register - user: {} successfully registered", registeredUser);
//        return registeredUser;
//    }
//
//    public UserModel findByLogin(String login) {
//        Optional<UserModel> userModel = userRepository.findByLogin(login);
//        UserModel model = userModel.orElseThrow(
//                () -> new NotFoundModelException(String.format("User with id = %s not found", login)));
//        return model;
//    }
//
//    public UserDto findById(Long id) {
//        Optional<UserModel> userDto = userRepository.findById(id);
//        UserModel userModel = userDto.orElseThrow(
//                () -> new NotFoundModelException(String.format("User with id = %s not found", id)));
//        log.info("IN findById -  User with id = {} found", userModel.getId());
//        return userMapper.mapFromEntity(userModel);
//    }
//
//    public UserDto save(UserDto userDto) {
//        validateAlreadyExists(null, userDto);
//        UserModel userModel = userMapper.mapFromDto(userDto);
//        UserModel user = userRepository.save(userModel);
//        log.info("IN save -  User with name  '{}' saved", userDto.getName());
//        return userMapper.mapFromEntity(user);
//    }
//
//    public UserDto update(UserDto userDto, Long id) {
//        validateAlreadyExists(id, userDto);
//        UserModel user = userMapper.mapFromDto(userDto);
//        user.setId(id);
//        UserModel userModel = userRepository.save(user);
//        log.info("IN update -  User  '{}' , updated", userDto.getName());
//        return userMapper.mapFromEntity(userModel);
//    }
//
//    public void deleteById(Long id) {
//        if (userRepository.findById(id).isPresent()) {
//            userRepository.deleteById(id);
//        } else {
//            throw new EntityNotFoundException(String.format("User with id = %s not found", id));
//        }
//        log.info("IN deleteById - User with id = {} deleted", id);
//    }
//
//    public List<UserDto> findAll() {
//        List<UserModel> userModels = userRepository.findAll();
//        log.info("IN findAll - the number of actors according to these criteria = {}", userModels.size());
//        return userMapper.mapFromListEntity(userModels);
//    }

//    private void validateAlreadyExists(Long id, UserDto dto) {
//        Optional<UserModel> check = userRepository.findByLogin(dto.getLogin());
//        if (check.isPresent() && !Objects.equals(check.get().getId(), id)) {
//            throw new DuplicateException(String.format("User with id = %s already exist", id));
//        }
//    }
}
