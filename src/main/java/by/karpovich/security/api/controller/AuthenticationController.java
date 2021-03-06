package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.AuthenticationRequestDto;
import by.karpovich.security.api.dto.AuthenticationResponseDto;
import by.karpovich.security.api.dto.RefreshTokenDto;
import by.karpovich.security.jpa.model.User;
import by.karpovich.security.security.jwt.JwtTokenProvider;
import by.karpovich.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String login = requestDto.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, requestDto.getPassword()));
            User user = userService.findByLogin(login);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }

            String token = jwtTokenProvider.createToken(login, user.getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(login, user.getRoles());

            AuthenticationResponseDto response = new AuthenticationResponseDto();
            response.setLogin(login);
            response.setToken(token);
            response.setRefreshToken(refreshToken);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity getNewRefreshToken(@RequestBody RefreshTokenDto refreshToken) throws AuthException {

        if (jwtTokenProvider.validateRefreshToken(refreshToken.getRefreshToken())) {
            String login = jwtTokenProvider.getUsername(refreshToken.getRefreshToken());
            User user = userService.findByLogin(login);

            if (user != null) {
                String token = jwtTokenProvider.createToken(login, user.getRoles());
                String newRefreshToken = jwtTokenProvider.createRefreshToken(login, user.getRoles());

                AuthenticationResponseDto response = new AuthenticationResponseDto();
                response.setLogin(login);
                response.setToken(token);
                response.setRefreshToken(newRefreshToken);

                return new ResponseEntity(response, HttpStatus.OK);
            }
        }
        throw new AuthException("???????????????????? JWT ??????????");
    }

    @PostMapping("/token")
    public ResponseEntity getNewToken(@RequestBody RefreshTokenDto refreshToken) throws AuthException {

        if (jwtTokenProvider.validateRefreshToken(refreshToken.getRefreshToken())) {
            String login = jwtTokenProvider.getUsername(refreshToken.getRefreshToken());
            User user = userService.findByLogin(login);

            if (user != null) {
                String token = jwtTokenProvider.createToken(login, user.getRoles());

                AuthenticationResponseDto response = new AuthenticationResponseDto();
                response.setLogin(login);
                response.setToken(token);

                return new ResponseEntity(response, HttpStatus.OK);
            }
        }
        throw new AuthException("???????????????????? JWT ??????????");
    }


    @PostMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
