package com.kafkaquest.kq.api.KafkaQuestApiService.controller;

import com.kafkaquest.kq.api.KafkaQuestApiService.dto.PlayerLoginRequest;
import com.kafkaquest.kq.api.KafkaQuestApiService.model.Player;
import com.kafkaquest.kq.api.KafkaQuestApiService.service.PlayerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/player")
@AllArgsConstructor
@Slf4j(topic = "[PlayerController]")
public class PlayerController {

    private final PlayerService playerService;

    @CrossOrigin(origins = "*")
    @PostMapping("register")
    public ResponseEntity<Player> register(@RequestBody @Valid Player player) {
        final var createdPlayer = playerService.createPlayer(player);
        final var uri = URI.create("/player/register");
        return ResponseEntity.created(uri).body(createdPlayer);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("login")
    public ResponseEntity<Player> login(@RequestBody @Valid PlayerLoginRequest playerLoginRequest) {
        final var player = playerService.login(playerLoginRequest.getUsername());
        return ResponseEntity.ok(player);
    }

}
