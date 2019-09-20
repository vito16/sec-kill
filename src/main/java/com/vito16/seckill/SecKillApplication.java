package com.vito16.seckill;

import com.vito16.seckill.service.SecKillService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author vito
 */
@SpringBootApplication
public class SecKillApplication implements InitializingBean {


    @Autowired
    RedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class, args);
    }

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.opsForValue().set(SecKillService.KC,"10");
    }
}
