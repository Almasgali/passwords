package ru.almasgali.passwords.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordRequest {
    private String name;
    private String password;
    private String comment;
}
