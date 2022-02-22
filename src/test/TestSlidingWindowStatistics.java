package test;

import main.ProbabilisticRandomGen.ProbabilisticRandomGen;
import main.ProbabilisticRandomGen.SimpleProbabilisticRandomGen;
import main.SlidingWindowStatistics.SimpleSlidingWindowStatistics;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestSlidingWindowStatistics {

    @Test
    public void SlidingWindowStatisticsTest () throws InterruptedException {
        SimpleSlidingWindowStatistics s = new SimpleSlidingWindowStatistics(100,1000);
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);
        scheduledExecutor.scheduleAtFixedRate(()->{s.add(1);}, 0, 50, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(()->{s.add(2);}, 0, 47, TimeUnit.MILLISECONDS);
        SimpleSlidingWindowStatistics.StatEventListener sl = new SimpleSlidingWindowStatistics.StatEventListener();
        s.subscribeForStatistics(SimpleSlidingWindowStatistics.StatEvent.class,sl);
        Thread.sleep(100000);
    }

}