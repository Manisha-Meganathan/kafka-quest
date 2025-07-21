package com.kafkaquest.kq.api.KESAApiService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players", schema = "kesa_auth")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, message = "Username must be greater than 4 characters")
    @Column(name = "username", unique = true, nullable = false)
    private String username;
}
