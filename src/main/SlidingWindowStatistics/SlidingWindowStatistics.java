package main.SlidingWindowStatistics;

import main.EventBus.Event;
import main.EventBus.EventListener;

public interface SlidingWindowStatistics {
    void add(int val);
    // subscriber will have a callback that'll deliver a Statistics instance (push)
    void subscribeForStatistics(Class<? extends Event> clazz, EventListener listener);

    // get latest statistics (poll)
    Statistics getLatestStatistics();

    public interface Statistics {
        float getMean();
        Integer getMode();
        int getPctile(int pctile);
    }
}