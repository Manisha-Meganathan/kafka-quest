package com.kafkaquest.kq.api.KESAApiService.service;

import com.kafkaquest.kq.api.KESAApiService.exception.BadCredentialsException;
import com.kafkaquest.kq.api.KESAApiService.exception.UsernameExistsException;
import com.kafkaquest.kq.api.KESAApiService.exception.UsernameValidationFailed;
import com.kafkaquest.kq.api.KESAApiService.model.Player;
import com.kafkaquest.kq.api.KESAApiService.repository.PlayerRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j(topic = "[PlayerService]")
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player createPlayer(Player player) {
        /* TODO should use validators for this */
        if(player.getUsername().length()<5){
            throw new UsernameValidationFailed(
                    "Username is invalid : %s , UserName should least 5 characters long.".formatted(player.getUsername())
            );
        }
        if (playerRepository.existsByUsername(player.getUsername())) {
            throw new UsernameExistsException(
                "User with username: %s already exists".formatted(player.getUsername())
            );
        }
        return playerRepository.save(player);
    }

    public Player login(String username) {
        final var optionalPlayer = playerRepository.findByUsername(username);

        if (optionalPlayer.isEmpty()) {
            throw new BadCredentialsException("Invalid Username: %s , User doesn't exist.".formatted(username));
        }

        return optionalPlayer.get();
    }

}
