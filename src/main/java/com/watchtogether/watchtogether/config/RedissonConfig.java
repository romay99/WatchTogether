package com.watchtogether.watchtogether.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  @Value("${redis.hostUrl}")
  private String host;

  @Value("${redis.portNum}")
  private int port;

  private static final String REDISSON_HOST_PREFIX = "";

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port);

    return Redisson.create(config);
  }
}