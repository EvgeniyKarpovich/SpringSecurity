package by.karpovich.security.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @ApiModelProperty(value = "id", example = "1", position = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ApiModelProperty(value = "Name", example = "", required = true, position = 2)
    @NotBlank(message = "Enter Name")
    private String login;

    @ApiModelProperty(value = "FirstName", example = "", required = true, position = 3)
    @NotBlank(message = "Enter firstName")
    private String firstName;

    @ApiModelProperty(value = "LastName", example = "", required = true, position = 4)
    @NotBlank(message = "Enter lastName")
    private String lastName;


    @ApiModelProperty(value = "Email", example = "", required = true, position = 5)
    @NotBlank(message = "Enter email")
    private String email;
}
