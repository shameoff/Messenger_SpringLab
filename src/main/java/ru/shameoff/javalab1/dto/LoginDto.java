package ru.shameoff.javalab1.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class LoginDto {
    @NotNull
    private final String username;
    @NotNull
    private final String password;
}
