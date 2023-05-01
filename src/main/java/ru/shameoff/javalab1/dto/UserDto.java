package ru.shameoff.javalab1.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;
    private String login;
    private String email;
    private String fullName;
    private Date birthDate;
    private String phoneNumber;
    private String city;
    private String avatarUuid;
}
