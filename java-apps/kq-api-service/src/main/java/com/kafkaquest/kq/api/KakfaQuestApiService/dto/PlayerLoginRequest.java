package com.kafkaquest.kq.api.KESAApiService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerLoginRequest {

    @NotBlank(message = "Username can not be blank")
    private String username;
}
