package main.EventBus;

public class YEventListener extends EventListener {
    public void doEvent(Event e) {
        System.out.println(((YEvent)e).val + ((YEvent)e).yval);
    }
}
