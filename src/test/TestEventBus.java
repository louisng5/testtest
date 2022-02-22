package test;

import main.EventBus.*;
import main.Throttler.SimpleThrottler;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestEventBus {

    @Test
    public void SingleThreatEventBusTest()  {
        SingleThreadEventBus b = new SingleThreadEventBus();

        b.addSubscriber(XEvent.class,new XEventListener());
        b.addSubscriber(XEvent.class,new XEventListener());
        b.addSubscriber(YEvent.class,new YEventListener());
        b.addSubscriber(YEvent.class,new YEventListener());
        b.addSubscriber(YEvent.class,new YEventListener());
        b.addSubscriber(YEvent.class,new YEventListener());
        b.addSubscriber(YEvent.class,new YEventListener());
        b.publishEvent(new YEvent(5,10));
        b.publishEvent(new YEvent(5,10));
        b.publishEvent(new XEvent(5));



    }

    @Test
    public void SingleThreatEventBusWithFilterTest()  {
        SingleThreadEventBus b = new SingleThreadEventBus();

        System.out.println("Filter!!!!");

        YEventListener yl = new YEventListener();
        b.addSubscriber(YEvent.class,yl);
        b.addSubscriberForFilteredEvents(YEvent.class,yl,(Event e)->{
            return ((YEvent)e).getYval() > 10;}
        );
        b.publishEvent(new YEvent(5,9));
        b.publishEvent(new YEvent(5,11));

    }

    @Test
    public void MultiThreatEventBusTest()  {
        MultiThreadEventBus m = new MultiThreadEventBus();
        YEventListener yl = new YEventListener();
        m.addSubscriber(YEvent.class,yl);
        m.addSubscriberForFilteredEvents(YEvent.class,yl,(Event e)->{

            System.out.println("sleep thread:" + Thread.currentThread().getName());

            return ((YEvent)e).getYval() > 10;}
        );
        m.publishEvent(new YEvent(5,9));
        m.publishEvent(new YEvent(5,11));
        m.publishEvent(new YEvent(5,13));
        m.publishEvent(new YEvent(5,15));
        m.publishEvent(new YEvent(5,17));
        m.publishEvent(new YEvent(5,19));
        m.publishEvent(new YEvent(5,11));
        m.publishEvent(new YEvent(5,13));
        m.publishEvent(new YEvent(5,15));
        m.publishEvent(new YEvent(5,17));
        m.publishEvent(new YEvent(5,19));
        m.publishEvent(new YEvent(5,11));
        m.publishEvent(new YEvent(5,13));
        m.publishEvent(new YEvent(5,15));
        m.publishEvent(new YEvent(5,17));
        m.publishEvent(new YEvent(5,19));
        m.publishEvent(new YEvent(5,11));
        m.publishEvent(new YEvent(5,13));
        m.publishEvent(new YEvent(5,15));
        m.publishEvent(new YEvent(5,17));
        m.publishEvent(new YEvent(5,19));
        m.publishEvent(new YEvent(5,11));
        m.publishEvent(new YEvent(5,13));
        m.publishEvent(new YEvent(5,15));
        m.publishEvent(new YEvent(5,17));

    }

}