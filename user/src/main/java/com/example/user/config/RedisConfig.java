package com.example.user.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // 1. 연결 공장을 만듭니다. (이름을 붙여주면 나중에 다른 Redis와 구분하기 좋아요)
    @Bean(name = "tokenRedisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        
        return new LettuceConnectionFactory(configuration);
    }
    
    // 2. 실제 서비스에서 사용할 템플릿입니다.
    // 여기서 name = "tokenRedis"가 서비스의 @Qualifier("tokenRedis")와 연결됩니다!
    @Bean(name = "tokenRedis")
    public RedisTemplate<String, Object> redisTemplate(
            @Qualifier("tokenRedisConnectionFactory") RedisConnectionFactory rcf) {
        
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(rcf);

        // 글자 깨짐 방지 설정 (아주 잘 넣으셨어요!)
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}