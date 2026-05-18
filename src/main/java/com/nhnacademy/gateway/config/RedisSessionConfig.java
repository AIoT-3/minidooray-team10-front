package com.nhnacademy.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession // 기능 활성화
@Configuration
public class RedisSessionConfig {}