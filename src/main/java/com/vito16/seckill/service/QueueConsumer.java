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

import static com.vito16.seckill.service.SecKillService.*;

@Slf4j
@Service
public class QueueConsumer  implements ApplicationRunner {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            RLock lock = redissonClient.getLock(KILL_SINGLE);
            try {
                lock.tryLock(30, 10, TimeUnit.SECONDS);
                String name = (String) redisTemplate.opsForList().leftPop(KILL_QUEUE);
                log.info("当前抢购用户：{}", name);
                String kcStr = (String) redisTemplate.opsForValue().get(KC);
                Integer kc = Integer.valueOf(kcStr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(kc<1){
                    redisTemplate.delete(KILL_QUEUE);
                    log.error("库存秒杀完毕，下次请早.");
                    break;
                }
                log.info("当前库存{}，秒杀后库存为：{}", kc, kc - 1);
                redisTemplate.opsForValue().set(KC, String.valueOf(kc - 1));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        log.info("秒杀结束...");

    }
}
