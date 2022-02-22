package test;

import main.Throttler.SimpleThrottler;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestThrottler {

    @Test
    public void SimpleThrottlerTest() throws InterruptedException {
        System.out.println("Test");
        SimpleThrottler t = new SimpleThrottler(10,100,100);
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);
        scheduledExecutor.scheduleAtFixedRate(()->{System.out.println(t.shouldProceed());}, 0, 10, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(()->{System.out.println(t.shouldProceed());}, 2, 10, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(()->{System.out.println(t.shouldProceed());}, 4, 10, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(()->{System.out.println(t.shouldProceed());}, 6, 10, TimeUnit.MILLISECONDS);

        Thread.sleep(10000);

    }

}