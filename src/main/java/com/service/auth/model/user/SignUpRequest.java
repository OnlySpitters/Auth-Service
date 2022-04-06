package com.service.auth.model.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
    @Size(max = 25)
    private String firstname;

    @Size(max = 25)
    private String lastname;

    @NotBlank
    @Size(max = 128)
    @Email
    private String email;

    @NotBlank
    @Size(max = 30)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
