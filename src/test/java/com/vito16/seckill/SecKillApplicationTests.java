package com.vito16.seckill;

import com.vito16.seckill.service.SecKillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecKillApplicationTests {

    @Autowired
    SecKillService secKillService;

    @Test
    public void contextLoads() {
        ExecutorService es = Executors.newFixedThreadPool(2);

        Future<Integer> f1 = null;
        for (int i = 1; i < 20; i++) {
            int userIndex = i;
            f1 = es.submit(() -> {
                secKillService.seckill("USER:"+ userIndex);
                return 1;
            });
        }

        try {
            f1.get(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }

}
