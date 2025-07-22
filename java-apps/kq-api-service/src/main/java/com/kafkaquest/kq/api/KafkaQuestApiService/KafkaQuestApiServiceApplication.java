package com.kafkaquest.kq.api.KafkaQuestApiService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaQuestApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaQuestApiServiceApplication.class, args);
	}

}
