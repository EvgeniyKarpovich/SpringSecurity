package by.karpovich.security.api.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDto {

    private String login;
    private String token;

}
