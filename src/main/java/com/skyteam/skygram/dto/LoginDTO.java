package com.skyteam.skygram.dto;

import javax.validation.constraints.NotBlank;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
