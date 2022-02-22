package main.SlidingWindowStatistics;

import main.EventBus.Event;
import main.EventBus.EventListener;
import main.EventBus.MultiThreadEventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class SimpleSlidingWindowStatistics implements SlidingWindowStatistics {
    int updateIntervalMs;
    int slidingWindowMs;
    int numBlock;
    private SimpleStatistics stat = null;
    private MultiThreadEventBus mb;
    Block lastBlock;

    LinkedBlockingQueue<Block> q;
    ScheduledExecutorService scheduledExecutor;
    ExecutorService executorService;

    public SimpleSlidingWindowStatistics(int updateintervalMs,int slidingwindowMs){
        scheduledExecutor = Executors.newScheduledThreadPool(1);
        executorService = Executors.newFixedThreadPool(4);
        mb = new MultiThreadEventBus();

        this.updateIntervalMs = updateintervalMs;
        this.slidingWindowMs = slidingwindowMs;
        if (slidingwindowMs % updateintervalMs != 0){
            throw new RuntimeException("slidingwindowMs % updateintervalMs != 0");
        }
        this.numBlock = (slidingwindowMs / updateintervalMs) + 1;
        this.q = new LinkedBlockingQueue<>();
        for (int i = 0; i < this.numBlock; i++) {
            lastBlock = new Block();
            this.q.add(lastBlock);
        }

        scheduledExecutor.scheduleAtFixedRate(this::iterate, 0, updateIntervalMs, TimeUnit.MILLISECONDS);
    }

    private void iterate(){
        lastBlock = new Block();
        this.q.add(lastBlock);
        executorService.execute(() -> {
            updateStat(q.toArray());
        });
        q.poll();
    }

    private void updateStat(Object[] arr){
        List<Block> data = new ArrayList<>();
        for (int i = 0; i < this.numBlock - 1; i++) {
            Block block = (Block)arr[i];
            data.add(block);
        }
        stat = new SimpleStatistics(data);
        mb.publishEvent(new StatEvent(stat));
    }

    @Override
    public void add(int val) {
        executorService.execute(()->{lastBlock.add(val);});
    }

    @Override
    public void subscribeForStatistics(Class<? extends Event> clazz, EventListener listener) {
        mb.addSubscriber(clazz,listener);
    }

    @Override
    public Statistics getLatestStatistics() {
        return stat;
    }

    public class Block{
        List<Integer> data;
        boolean summarized = false;
        int sum = 0;
        int count = 0;
        HashMap<Integer,Integer> wordCount;

        public Block(){
            data = new ArrayList<>();
            wordCount = new HashMap();
        }

        public synchronized void add(int val){
            data.add(val);
        }

        public void summarize(){
            if (summarized){
                return;
            }

            count = data.size();
            for (Integer i:data){
                if (i==null){
                    System.out.println(data);
                }
                sum += i;
                if (wordCount.get(i) != null) {

                    int count = wordCount.get(i);
                    count++;
                    wordCount.put(i, count);
                }
                else
                    wordCount.put(i,1);
            }
            summarized = true;
        }
    }

    public class SimpleStatistics implements Statistics{
        int sum;
        int count;
        Integer mode;
        List<Block> dataList;
        HashMap<Integer,Integer> wordCount;

        public SimpleStatistics(List<Block>data){
            dataList = data;
            count = 0;
            sum = 0;
            wordCount = new HashMap<Integer,Integer>();
            mode = null;
            for(Block block: data) {
                block.summarize();
                sum += block.sum;
                count += block.count;
                block.wordCount.forEach((k,v)->{
                    Integer cnt = wordCount.get(k);
                    if (cnt == null){
                        wordCount.put(k,v);
                    } else {
                        wordCount.put(k,v + cnt);
                    }
                });
            }
            int maxCount = 0;
            for (int key :wordCount.keySet()){
                int keycount = wordCount.get(key);
                if (keycount > maxCount){
                    maxCount = keycount;
                    mode = key;
                }
            }


        }
        @Override
        public float getMean() {
            return (float) sum/count;
        }

        @Override
        public Integer getMode() {
            return mode;
        }

        @Override
        public int getPctile(int pctile) {
            return 0;
        }
    }
    public class StatEvent extends Event {
        public Statistics stat;
        public StatEvent(Statistics pstat) {
            stat = pstat;
        }
    }
    public static class StatEventListener extends EventListener {
        public void doEvent(Event e) {
            System.out.println("Mode:" + ((StatEvent)e).stat.getMode());
            System.out.println("Mean:" + ((StatEvent)e).stat.getMean());
        }
    }
}
