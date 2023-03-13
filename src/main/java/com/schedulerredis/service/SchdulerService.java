package com.schedulerredis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SchdulerService {

	private static final String REDIS_KEY = "Saint";

	@Autowired
	private RedisTemplate<String, String> template;

	// running every one minute CRON job
	@Scheduled(cron = "1 * * * * *")
	public void createKey() {
		log.info(SchdulerService.class.getSimpleName() + " " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
				+ " is running");
		String value = String.valueOf(UUID.randomUUID());
		template.opsForSet().add(REDIS_KEY, value);
		template.expire(REDIS_KEY, Duration.ofSeconds(60));
		template.afterPropertiesSet();
	}

	// logging every 10 seconds for key and expiry
	@Scheduled(cron = "1/10 * * * * *")
	public void getKey() {
		log.info("Get Redis Key : " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + " is running");
		log.info(template.keys(REDIS_KEY) + " xx " + template.getExpire(REDIS_KEY));

	}
	
	// Key Expiring
	// -1 means no expiry
	// -2 means expiry
	// 10, 20, 30 means expiry seconds
	
	// redis cli
	// KEYS * --> Get All keys
	// TTL <key name> --> check key's expiry
	
}
