package com.skyteam.skygram.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "Account is required")
    private String account;

    @NotBlank(message = "Password is required")
    private String password;

    public LoginDTO(@NotBlank(message = "Account is required") String account,
                    @NotBlank(message = "Password is required") String password) {
        this.account = account;
        this.password = password;
    }
}
