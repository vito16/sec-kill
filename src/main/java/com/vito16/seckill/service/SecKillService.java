package com.vito16.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author vito
 */
@Slf4j
@Service
public class SecKillService {

    /**
     * 库存Key
     */
    public static final String KC = "KC";

    /**
     * 秒杀竞争控制Key
     */
    public static final String KILL_SINGLE = "KILL_SINGLE";

    /**
     * 秒杀队列key
     */
    public static final String KILL_QUEUE = "KILL_QUEUE";

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisTemplate redisTemplate;


    public void seckill(String name) {
        redisTemplate.opsForList().rightPush(KILL_QUEUE, name);
    }
}
