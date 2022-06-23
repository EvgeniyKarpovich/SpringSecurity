package by.karpovich.security.api.dto;

import by.karpovich.security.api.validation.PasswordMatches;
import by.karpovich.security.api.validation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@PasswordMatches
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistryDto {

    @ApiModelProperty(value = "id", example = "1", position = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ApiModelProperty(value = "Name", example = "", required = true, position = 2)
    @NotBlank(message = "Enter Name")
    private String login;

    @ApiModelProperty(value = "Password", example = "", required = true, position = 3)
    @NotBlank(message = "Enter password")
    private String password;

//    @NotBlank(message = "Enter matchingPassword")
//    private String matchingPassword;

    @ApiModelProperty(value = "FirstName", example = "", required = true, position = 4)
    @NotBlank(message = "Enter firstName")
    private String firstName;

    @ApiModelProperty(value = "LastName", example = "", required = true, position = 5)
    @NotBlank(message = "Enter lastName")
    private String lastName;

    @ApiModelProperty(value = "Email", example = "", required = true, position = 6)
    @ValidEmail
    @NotBlank(message = "Enter email")
    private String email;

    @ApiModelProperty(value = "avatar", example = "", position = 7)
    private String avatar;
}

