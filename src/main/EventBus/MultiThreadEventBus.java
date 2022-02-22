package main.EventBus;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class MultiThreadEventBus implements EventBus {

    ConcurrentHashMap<Class<? extends Event>, ConcurrentHashMap<EventListener,HashSet<Function<Event,Boolean>>>> subscriberMap;
    public MultiThreadEventBus(){
        subscriberMap = new ConcurrentHashMap<>();
    }

    @Override
    public void publishEvent(Event e) {
        ExecutorService executorService =
                Executors.newFixedThreadPool(4);
        ConcurrentHashMap<EventListener, HashSet<Function<Event,Boolean>>>  map = subscriberMap.get(e.getClass());
        if (map != null){
            map.forEach(1,(key, value) -> {
                executorService.execute(() -> {
                    System.out.println("Publish!" + Thread.currentThread().getName());
                    publish(key, value,e.getClass().cast(e));
                    System.out.println("Publish done thread:" + Thread.currentThread().getName());
                });
            }
            );
        }
    }

    private void publish(EventListener l,HashSet<Function<Event,Boolean>> set,Event e) {
        boolean go = true;
        for (Function<Event,Boolean> func:set){
            if (!func.apply(e)){
                go = false;
            }
        }
        if (go){
            l.doEvent(e);
        }
    }

    @Override
    public void addSubscriber(Class<? extends Event> clazz, EventListener listener) {
        if (!subscriberMap.containsKey(clazz)){
            ConcurrentHashMap<EventListener, HashSet<Function<Event, Boolean>>> map = new ConcurrentHashMap<>();
            map.put(listener,new HashSet<Function<Event,Boolean>>());
            subscriberMap.put(clazz,map);
        } else {
            ConcurrentHashMap<EventListener, HashSet<Function<Event, Boolean>>> map = subscriberMap.get(clazz);
            if (!map.containsKey(listener)){
                map.put(listener,new HashSet<Function<Event,Boolean>>());
            }
        }

    }

    @Override
    public void addSubscriberForFilteredEvents(Class<? extends Event> clazz, EventListener listener, Function<Event,Boolean> func) {
        ConcurrentHashMap<EventListener, HashSet<Function<Event, Boolean>>> map = subscriberMap.get(clazz);
        if (map == null){
            throw new RuntimeException("Not subscribed");
        }
        HashSet<Function<Event,Boolean>> set = map.get(listener);
        if (set == null){
            throw new RuntimeException("Not subscribed");
        }
        set.add(func);
    }

}
