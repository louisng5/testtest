package main.Throttler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleThrottler implements Throttler{
    int refillAmt;
    int bucket;
    int maxBucket;
    ScheduledExecutorService scheduledExecutor;
    ExecutorService executorService;

    public SimpleThrottler(int refillamt,int refillIntervalMS,int maxbucket){
        refillAmt = refillamt;
        bucket = maxbucket;
        maxBucket = maxbucket;
        scheduledExecutor = Executors.newScheduledThreadPool(1);
        executorService = Executors.newFixedThreadPool(4);
        scheduledExecutor.scheduleAtFixedRate(this::refill, 0, refillIntervalMS, TimeUnit.MILLISECONDS);
    }

    private synchronized void refill(){
        int newBucket = bucket + refillAmt;
        bucket = newBucket > maxBucket? maxBucket:newBucket;
    }

    private synchronized void poll(){
        bucket -= 1;
    }

    @Override
    public ThrottleResult shouldProceed() {
        if (bucket > 0){
            executorService.execute(this::poll);
            return ThrottleResult.PROCEED;
        } else {
            return ThrottleResult.DO_NOT_PROCEED;
        }
    }

    @Override
    public void notifyWhenCanProceed() {

    }
}
