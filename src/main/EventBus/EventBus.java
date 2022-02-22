package main.EventBus;

import java.util.function.Function;

public interface EventBus {

    // Feel free to replace Object with something more specific,
// but be prepared to justify it
    void publishEvent(Event e);
    // How would you denote the subscriber?
    void addSubscriber(Class<? extends Event> clazz, EventListener listener);
    // Would you allow clients to filter the events they receive? How would the interface look like?
    void addSubscriberForFilteredEvents(Class<? extends Event> clazz, EventListener listener, Function<Event,Boolean> func);

}