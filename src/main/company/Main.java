package main.company;
import main.EventBus.*;
import main.SlidingWindowStatistics.SimpleSlidingWindowStatistics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
//        SimpleProbabilisticRandomGen g = new SimpleProbabilisticRandomGen();
//        List<NumAndProbability>  lst = new ArrayList<>();
//        lst.add(new NumAndProbability(1,0.f));
//        lst.add(new NumAndProbability(2,0.5f));
//        g.loadSample(lst);
//        for (int i = 0; i < 100; i++) {
//            System.out.println(g.nextFromSample());
//        }



        SimpleSlidingWindowStatistics s = new SimpleSlidingWindowStatistics(100,1000);
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);
        scheduledExecutor.scheduleAtFixedRate(()->{s.add(1);}, 0, 50, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(()->{s.add(2);}, 0, 50, TimeUnit.MILLISECONDS);
        SimpleSlidingWindowStatistics.StatEventListener sl = new SimpleSlidingWindowStatistics.StatEventListener();
        s.subscribeForStatistics(SimpleSlidingWindowStatistics.StatEvent.class,sl);
        Thread.sleep(100000);

    }
}
