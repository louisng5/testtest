package main.EventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class SingleThreadEventBus implements EventBus {

    HashMap<Class<? extends Event>, HashMap<EventListener,HashSet<Function<Event,Boolean>>>> subscriberMap;
    public SingleThreadEventBus(){
        subscriberMap = new HashMap<>();
    }

    @Override
    public void publishEvent(Event e) {

        HashMap<EventListener, HashSet<Function<Event,Boolean>>>  map = subscriberMap.get(e.getClass());
        if (map != null){
            map.forEach((key, value) -> {
                        publish(key, value,e.getClass().cast(e));
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
//        System.out.println(go);
        if (go){
            l.doEvent(e);
        }
    }

    @Override
    public void addSubscriber(Class<? extends Event> clazz, EventListener listener) {
        if (!subscriberMap.containsKey(clazz)){
            HashMap<EventListener, HashSet<Function<Event, Boolean>>> map = new HashMap<>();
            map.put(listener,new HashSet<Function<Event,Boolean>>());
            subscriberMap.put(clazz,map);
        } else {
            HashMap<EventListener, HashSet<Function<Event, Boolean>>> map = subscriberMap.get(clazz);
            if (!map.containsKey(listener)){
                map.put(listener,new HashSet<Function<Event,Boolean>>());
            }
        }

    }

    @Override
    public void addSubscriberForFilteredEvents(Class<? extends Event> clazz, EventListener listener, Function<Event,Boolean> func) {
        HashMap<EventListener, HashSet<Function<Event, Boolean>>> map = subscriberMap.get(clazz);
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
